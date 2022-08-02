package com.mysite.sbbb.question.service;

import com.mysite.sbbb.DataNotFoundException;
import com.mysite.sbbb.question.dao.QuestionRepository;
import com.mysite.sbbb.question.domain.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {
    @Autowired
    private QuestionRepository questionRepository;

    public List<Question> getList() {
        return questionRepository.findAll();
    }

    public Page<Question> getList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.questionRepository.findAll(pageable);
    }

    public Question getQuestion(Integer id) {
        Optional<Question> question = this.questionRepository.findById(id);
        if (question.isPresent()) {
            return question.get();
        } else {
            throw new DataNotFoundException("question not found");
        }
    }

    public void create(Question question) {
        Question questions = new Question();
        questions.setContent(question.getContent());
        questions.setCreateDate(LocalDateTime.now());
        questions.setReplyLike(false);
        questionRepository.save(questions);
    }

/*

        String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";

        UUID uuid = UUID.randomUUID();

        String fileName = uuid + "_" + file.getOriginalFilename();

        File saveFile = new File(projectPath, fileName);

        file.transferTo(saveFile);

        question.setFilename(fileName);

        question.setFilepath("/files/" + fileName);

        question.setCreateDate(LocalDateTime.now());

        question.setReplyLike(false);

        questionRepository.save(question);
    }*/

    public void updateHit(Integer id) {
        Question question = getQuestion(id);
        int count = question.getHit();
        question.setHit(count + 1);
        questionRepository.save(question);
    }

    public void setLike(Integer questionId) {
        Question question = questionRepository.findById(questionId).get();

        if(question.getReplyLike() == true){
            question.setReplyLike(false);
        }else {
            question.setReplyLike(true);
        }
        this.questionRepository.save(question);
    }

}
