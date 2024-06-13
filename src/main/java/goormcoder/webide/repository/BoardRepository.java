package goormcoder.webide.repository;

import goormcoder.webide.common.dto.ErrorMessage;
import goormcoder.webide.domain.Board;
import goormcoder.webide.domain.Member;
import goormcoder.webide.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    default Board findByIdOrThrow(final Long boardId) {
        return findByIdAndDeletedAtIsNull(boardId).orElseThrow(() -> new NotFoundException(ErrorMessage.BOARD_NOT_FOUND));
    }

    Optional<Board> findByIdAndDeletedAtIsNull(Long boardId);

    List<Board> findAllByDeletedAtIsNull();
}