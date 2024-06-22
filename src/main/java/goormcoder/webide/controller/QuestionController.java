package goormcoder.webide.controller;

import goormcoder.webide.domain.Question;
import goormcoder.webide.dto.response.QuestionFindDto;
import goormcoder.webide.dto.response.QuestionSummaryDto;
import goormcoder.webide.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Question", description = "문제 관련 API")
@RequiredArgsConstructor
@RequestMapping("/questions")
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping()
    @Operation(summary = "문제 전체 조회", description = "전체 문제를 조회합니다.")
    public ResponseEntity<Page<QuestionSummaryDto>> getAllQuestions(Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(questionService.getAllQuestions(pageable));
    }

    @Operation(summary = "문제 열람")
    @GetMapping("/{questionId}")
    public ResponseEntity<QuestionFindDto> findById(@PathVariable Long questionId) {
        Question question = questionService.findById(questionId);
        return ResponseEntity.ok(QuestionFindDto.of(question));
    }

}
