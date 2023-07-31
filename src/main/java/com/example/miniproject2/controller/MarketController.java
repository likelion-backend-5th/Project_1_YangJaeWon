package com.example.miniproject2.controller;



import com.example.miniproject2.Dto.CommentDto;
import com.example.miniproject2.Dto.ItemAndNegWrapper;
import com.example.miniproject2.Dto.ItemDto;
import com.example.miniproject2.Dto.NegotiationDto;
import com.example.miniproject2.Entity.Comment;
import com.example.miniproject2.Entity.Item;
import com.example.miniproject2.Entity.Negotiation;
import com.example.miniproject2.Repository.CommentRepository;
import com.example.miniproject2.Repository.ItemRepository;
import com.example.miniproject2.Repository.NegotiationRepository;
import com.example.miniproject2.Service.MarketService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class MarketController {
    private final MarketService marketService;
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;
    private final NegotiationRepository negotiationRepository;

    @PostMapping
    public ItemDto create(@RequestBody ItemDto dto, Model model) {
        model.addAttribute("message", "등록이 완료되었습니다.");
        ItemDto itemDto = marketService.createItem(dto);
        return itemDto;
    }

    //페이지 단위 조회
    @GetMapping
    public Page<ItemDto> readAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "limit", defaultValue = "20") Integer limit
    ) {
        return marketService.readItemPage(page, limit);
    }

    //id 별로 조회
    @GetMapping("/{id}")
    public ItemDto readOne(
            @PathVariable Long id
    ) {
        return marketService.readItem(id);
    }
    @PutMapping("/{id}")
    public ItemDto update(
            @PathVariable("id") Long id, @RequestBody ItemDto dto,
             Model model
    ) {
        ItemDto itemDto = marketService.updateItem(id, dto);
        if(itemDto == null) {
            model.addAttribute("message", "비밀번호가 일치하지 않습니다.");
            return null;
        } else {
            model.addAttribute("message", "물품이 수정되었습니다.");
            return itemDto;
        }

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id, @RequestBody ItemDto dto) {
        boolean deleted = marketService.deleteItem(id, dto);
        if (deleted) {
            return ResponseEntity.ok("물품이 삭제되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호가 일치하지 않습니다.");
        }
    }

    @PostMapping("/{id}/comments")
    public CommentDto createComment(@PathVariable("id") Long itemId, @RequestBody CommentDto dto, Model model) {
        model.addAttribute("message", "등록이 완료되었습니다.");
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new RuntimeException("Item not found"));
        dto.setItemId(item.getId());
        CommentDto commentDto = marketService.createComment(dto);
        return commentDto;
    }
    @GetMapping("/{id}/comments")
    public Page<CommentDto> readCommentAll(
            @PathVariable("id") Long itemId,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "limit", defaultValue = "20") Integer limit
    ) {
        return marketService.readCommentPage(itemId, page, limit);
    }

    @PutMapping("/{id}/comments/{commentId}")
    public CommentDto updateComment(@PathVariable("id") Long itemId,
                                    @PathVariable("commentId") Long commentId,
                                    @RequestBody CommentDto dto,
                                    Model model) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new RuntimeException("Item not found"));
        dto.setItemId(item.getId());
        CommentDto commentDto = marketService.updateComment(commentId, dto);
        if (commentDto == null) {
            model.addAttribute("message", "비밀번호가 일치하지 않습니다.");
            return null;
        } else {
            model.addAttribute("message", "댓글이 수정되었습니다.");
            return commentDto;
        }
    }
    @DeleteMapping("/{id}/comments/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable("id") Long itemId,
                                                @PathVariable("commentId") Long commentId,
                                                @RequestBody CommentDto dto) {
        boolean deleted = marketService.deleteComment(itemId, commentId, dto);
        if (deleted) {
            return ResponseEntity.ok("댓글이 삭제되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호가 일치하지않습니다.");
        }
    }
    @PutMapping("/{id}/comments/{commentId}/reply")
    public CommentDto updateReply(
            @PathVariable("id") Long itemId,
            @PathVariable("commentId") Long commentId,
            @RequestBody CommentDto dto,
            Model model) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new RuntimeException("Item not found"));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("Comment not found"));
        dto.setItemId(item.getId());
        dto.setCommentId(comment.getId());
        CommentDto commentDto = marketService.updateReply(dto);
        return commentDto;
    }

    @PostMapping("/{id}/proposals")
    public NegotiationDto createNeg(
            @PathVariable("id") Long itemId,
            @RequestBody NegotiationDto dto
    ) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new RuntimeException("Item not found"));
        dto.setItemId(item.getId());
        return marketService.createNeg(dto);
    }
    @GetMapping("/{id}/proposals")
    public ResponseEntity<Page<NegotiationDto>> readNegPage(
            @PathVariable("id") Long itemId,
            @RequestParam(value = "writer", required = false) String writer,
            @RequestParam(value = "password", required = false) String password,
            NegotiationDto dto,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "25") Integer size,
            String negWriter
        ) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Negotiation negotiation = negotiationRepository.findById(itemId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        negWriter = dto.getWriter();

        if(writer != null && password != null && negWriter != null) {
            if(!(writer.equals(item.getWriter()) && password.equals(item.getPassword())) && !(negWriter.equals(negotiation.getWriter()))) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "읽을 권한이 없습니다.");
            }
        }
        Page<NegotiationDto> negPage = marketService.readNegPage(itemId, page, size);

        return ResponseEntity.ok(negPage);
    }

    @PutMapping("/{id}/proposals/{proposalId}")
    public NegotiationDto updateNeg(
            @PathVariable("id") Long itemId,
            @PathVariable("proposalId") Long proposalId,
            @RequestBody ItemAndNegWrapper itemAndNegWrapper,
            Model model
    ) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new RuntimeException("Item not found"));
        ItemDto itemDto = itemAndNegWrapper.getItemDto();
        String writer = itemAndNegWrapper.getWriter();
        String password = itemAndNegWrapper.getPassword();
        String status = itemAndNegWrapper.getStatus();
        int suggestedPrice = itemAndNegWrapper.getSuggestedPrice();


        Negotiation negotiation = negotiationRepository.findById(proposalId).orElseThrow(() -> new RuntimeException("proposal not found"));
        NegotiationDto negotiationDto = NegotiationDto.fromEntity(negotiation);
        boolean priceChanged = suggestedPrice != negotiation.getSuggestedPrice();
        boolean statusChanged = false;
        if (status != null && negotiationDto.getStatus() != null) {
            statusChanged = !status.equals(negotiationDto.getStatus());
        }
        if (isNegWriter(proposalId, writer, password) && priceChanged) {
            negotiationDto.setSuggestedPrice(suggestedPrice);
            negotiationDto = marketService.updateNeg(proposalId, negotiationDto);
            model.addAttribute("message", "제안이 수정되었습니다.");
            return negotiationDto;
        }
        if (isItemWriter(itemId, writer, password) && statusChanged) {
            negotiationDto.setStatus(status);
            negotiationDto = marketService.updateNegFromItemWriter(itemId, proposalId, negotiationDto);
            model.addAttribute("message", "제안의 상태가 변경되었습니다."); //수락 or 거절
            if(status.equals("수락")) {
                statusChanged = false;
                if (itemAndNegWrapper.getStatus() != null && negotiationDto.getStatus() != null) {
                    statusChanged = !itemAndNegWrapper.getStatus().equals(negotiationDto.getStatus());
                }
                if (isNegWriter(proposalId, writer, password) && statusChanged) {
                    negotiationDto.setStatus(status);
                    negotiationDto = marketService.updateNegFromNegWriter(proposalId, negotiationDto);
                    model.addAttribute("message", "구매가 확정되었습니다."); //확정
                    return negotiationDto;
                }
            }
            return negotiationDto;
        }
        if (isNegWriter(proposalId, writer, password) && statusChanged) {
            negotiationDto.setStatus(status);
            negotiationDto = marketService.updateNegFromNegWriter(proposalId, negotiationDto);
            model.addAttribute("message", "구매가 확정되었습니다."); //확정
            return negotiationDto;
        }
        else {
            model.addAttribute("message", "수정 권한이 없습니다.");
            return null;
        }
    }
    @DeleteMapping("/{id}/proposals/{proposalId}")
    public ResponseEntity<String> deleteNeg(
            @PathVariable("id") Long itemId,
            @PathVariable("proposalId") Long proposalId,
            @RequestBody NegotiationDto dto
    ) {
        boolean deleted = marketService.deleteNeg(itemId, proposalId, dto);
        if (deleted) {
            return ResponseEntity.ok("제안이 삭제되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("삭제 권한이 없습니다.");
        }
    }

    //물품 등록한 유저의 작성자와 비밀번호 확인하는 메소드
    private boolean isItemWriter(Long itemId, String writer, String password) {
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        Item item;
        if (optionalItem.isPresent()) {
            item = optionalItem.get();
            return writer.equals(item.getWriter()) && password.equals(item.getPassword());
        }
        return false;
    }

    //제안 등록한 유저의 작성자와 비밀번호 확인하는 메소드
    private boolean isNegWriter(Long proposalId, String writer, String password) {
        Optional<Negotiation> optionalNegotiation = negotiationRepository.findById(proposalId);
        if (optionalNegotiation.isPresent()) {
            Negotiation negotiation = optionalNegotiation.get();
            return writer.equals(negotiation.getWriter()) && password.equals(negotiation.getPassword());
        }
        return false;
    }
}