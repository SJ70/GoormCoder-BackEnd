package goormcoder.webide.service;

import goormcoder.webide.constants.ErrorMessages;
import goormcoder.webide.domain.Question;
import goormcoder.webide.domain.enumeration.SolveResult;
import goormcoder.webide.dto.request.QuestionCreateDto;
import goormcoder.webide.dto.request.QuestionUpdateDto;
import goormcoder.webide.dto.response.QuestionFindAllDto;
import goormcoder.webide.dto.response.QuestionSummaryDto;
import goormcoder.webide.dto.response.SolveSummaryDto;
import goormcoder.webide.dto.response.TestCaseFindDto;
import goormcoder.webide.repository.QuestionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final MemberService memberService;

    @Transactional(readOnly = true)
    public List<QuestionFindAllDto> getAllQuestions(String loginId) {
        List<SolveSummaryDto> solvesByMember = memberService.findSolvesByLoginId(loginId);
        return QuestionFindAllDto.listOf(questionRepository.findAll(), solvesByMember, this);
    }

    @Transactional
    public QuestionSummaryDto create(QuestionCreateDto createDto) {
        Question question = new Question(createDto.title(), createDto.level(), createDto.content());
        questionRepository.save(question);
        return QuestionSummaryDto.of(question);
    }

    public Question findById(Long id) {
        return questionRepository.findById(id)
                .filter(Question::isActive)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.QUESTION_NOT_FOUND.getMessage()));
    }

    @Transactional
    public void save(Question question) {
        questionRepository.save(question);
    }

    public List<SolveSummaryDto> findSolvesById(Long id) {
        return this.findById(id)
                .getSolves()
                .stream()
                .map(SolveSummaryDto::of)
                .toList();
    }

    @Transactional
    public QuestionSummaryDto update(Long id, QuestionUpdateDto updateDto) {
        Question question = this.findById(id);
        question.update(updateDto.title(), updateDto.level(), updateDto.content());
        questionRepository.save(question);
        return QuestionSummaryDto.of(question);
    }

    @Transactional
    public void delete(Long id) {
        Question question = this.findById(id);
        question.markAsDeleted();
        questionRepository.save(question);
    }

    public List<TestCaseFindDto> findAllTestCasesById(Long id) {
        return this.findById(id)
                .getTestCases()
                .stream()
                .map(TestCaseFindDto::of)
                .toList();
    }

}
