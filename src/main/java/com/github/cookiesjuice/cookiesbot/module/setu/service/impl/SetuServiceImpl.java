package com.github.cookiesjuice.cookiesbot.module.setu.service.impl;

import com.github.cookiesjuice.cookiesbot.module.setu.dao.GradeRepository;
import com.github.cookiesjuice.cookiesbot.module.setu.dao.SetuRepository;
import com.github.cookiesjuice.cookiesbot.module.setu.dao.TagRepository;
import com.github.cookiesjuice.cookiesbot.module.setu.entity.*;
import com.github.cookiesjuice.cookiesbot.module.setu.service.SetuService;
import com.github.cookiesjuice.cookiesbot.utils.MD5Utils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Service
public class SetuServiceImpl implements SetuService {

    private final SetuRepository setuRepository;
    private final TagRepository tagRepository;
    private final GradeRepository gradeRepository;

    public SetuServiceImpl(SetuRepository setuRepository, TagRepository tagRepository, GradeRepository gradeRepository) {
        this.setuRepository = setuRepository;
        this.tagRepository = tagRepository;
        this.gradeRepository = gradeRepository;
    }

    @Override
    public Setu find(long id) {
        return setuRepository.findById(id).orElse(null);
    }

    @Override
    public File getFile(Setu setu) {
        String path = SetuService.SETU_PATH + "/" + setu.getFilename();
        File setuFile = new File(path);
        return setuFile.exists() ? setuFile : null;
    }

    @Override
    public synchronized Setu upload(User user, File file) {
        try {
            File path = new File(SETU_PATH);
            if (!path.exists()) {
                if (!path.mkdir()) return null;
            }
            String saveName = MD5Utils.getFileMD5String(file) + ".jpg";
            String savePath = SETU_PATH + "/" + saveName;
            Files.copy(file.toPath(), new File(savePath).toPath());

            List<Evaluate> evaluateList = evaluate(savePath);

            Setu setu = new Setu();
            setu.setFilename(saveName);
            setuRepository.save(setu);

            evaluateList.forEach(evaluate -> evaluate.setSetu(setu));
            setu.setEvaluates(evaluateList);
            setu.setUploadUser(user);
            return setuRepository.saveAndFlush(setu);
        } catch (IOException e) {
            //上传了md5相同的图
//            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean delete(Setu setu) {
        File file = getFile(setu);
        if (file != null && file.delete()) {
            setuRepository.delete(setu);
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(Long id) {
        Setu setu = find(id);
        return setu != null && delete(setu);
    }

    @Override
    public List<Setu> random(int num) {
        long count = setuRepository.count();
        if (count == 0) {
            return null;
        }
        if (count < num) {
            num = (int) count;
        }

        int page = (int) (Math.random() * count / num);

        Pageable pageable = PageRequest.of(page, num);
        return setuRepository.findAll(pageable).toList();
    }

    @Override
    public Setu random() {
        List<Setu> setuList = random(1);
        return setuList != null && setuList.size() > 0 ? setuList.get(0) : null;
    }

    @Override
    public synchronized List<Evaluate> evaluate(String imgPath) {
        List<String> retList = new ArrayList<>();
        String[] cmds = {"deepdanbooru", "evaluate", imgPath, "--project-path", "DeepDanbooru-cookies-bot"};

        try {
            Process process = new ProcessBuilder(cmds).start();
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;

            while ((line = br.readLine()) != null) {
                retList.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Evaluate> evaluateList = new ArrayList<>();

        for (int i = 1; i < retList.size() - 1; i++) {
            String tagData = retList.get(i);
            String[] tagDatas = tagData.split(" ");
            double data = Double.parseDouble(tagDatas[0].replaceAll("[()]", ""));
            String name = tagDatas[1];
            Tag tag = new Tag();
            tag.setName(name);
            if (tagRepository.findById(name).orElse(null) == null) {
                tagRepository.saveAndFlush(tag);
            }

            Evaluate evaluate = new Evaluate();
            evaluate.setTag(tag);
            evaluate.setReliability(data);
            evaluateList.add(evaluate);
        }

        return evaluateList;
    }

    @Override
    public synchronized int grade(User user, Setu setu, int score) {

        for (Grade g : setu.getGrades()) {
            if (g.getUser().getId() == user.getId()) {
                int old = g.getScore();

                g.setScore(score);
                gradeRepository.saveAndFlush(g);
                return old;
            }
        }

        Grade grade = new Grade();
        grade.setScore(score);
        gradeRepository.save(grade);

        grade.setUser(user);
        grade.setSetu(setu);
        gradeRepository.saveAndFlush(grade);

        return 0;
    }
}
