package com.example.miniproject2.Service;

import com.example.miniproject2.Dto.CommentDto;
import com.example.miniproject2.Dto.ItemDto;
import com.example.miniproject2.Dto.NegotiationDto;
import com.example.miniproject2.Entity.Comment;
import com.example.miniproject2.Entity.Item;
import com.example.miniproject2.Entity.Negotiation;
import com.example.miniproject2.Repository.CommentRepository;
import com.example.miniproject2.Repository.ItemRepository;
import com.example.miniproject2.Repository.NegotiationRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Builder
@RequiredArgsConstructor
@Service
public class MarketService {
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;
    private final NegotiationRepository negotiationRepository;
    //물품등록
    public ItemDto createItem(ItemDto dto) {
        Item newItem = new Item();
        newItem.setTitle(dto.getTitle());
        newItem.setDescription(dto.getDescription());
        newItem.setWriter(dto.getWriter());
        newItem.setPassword(dto.getPassword());
        newItem.setMin_price_wanted(dto.getMinPriceWanted());
        newItem.setStatus("판매중");
        return ItemDto.fromEntity(itemRepository.save(newItem));
    }
    public Page<ItemDto> readItemPage(
            Integer pageNumber, Integer pageSize
    ) {
        Pageable pageable = PageRequest.of(
                pageNumber, pageSize, Sort.by("id").descending());
        Page<Item> itemPage = itemRepository.findAll(pageable);
        Page<ItemDto> itemDtoPage = itemPage.map(ItemDto::fromEntity);
        return itemDtoPage;
    }

    public ItemDto readItem(Long id) {
        Optional<Item> optionalItem = itemRepository.findById(id);
        if(optionalItem.isPresent()) {
            return ItemDto.fromEntity(optionalItem.get());
        }
        else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    public ItemDto updateItem(Long id, ItemDto dto) {
        Optional<Item> optionalItem = itemRepository.findById(id);
        Item item;
        if (optionalItem.isPresent()) {
            item = optionalItem.get();
            if (!dto.getPassword().equalsIgnoreCase(item.getPassword())) {
                return null;
            }
            item.setTitle(dto.getTitle());
            item.setDescription(dto.getDescription());
            item.setMin_price_wanted(dto.getMinPriceWanted());
            item.setWriter(dto.getWriter());
            return ItemDto.fromEntity(itemRepository.save(item));
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public boolean deleteItem(Long id, ItemDto dto) {
        Optional<Item> optionalItem = itemRepository.findById(id);
        if (optionalItem.isPresent()) {
            Item item = optionalItem.get();
            if (dto.getPassword().equalsIgnoreCase(item.getPassword())) {
                itemRepository.delete(item);
                return true;
            }
        }
        return false;
    }

    public CommentDto createComment(CommentDto dto) {
        Comment newComment = new Comment();
        newComment.setWriter(dto.getWriter());
        newComment.setContent(dto.getContent());
        newComment.setPassword(dto.getPassword());
        newComment.setItemId(dto.getItemId());
        return CommentDto.fromEntity(commentRepository.save(newComment));
    }
    public Page<CommentDto> readCommentPage(Long itemId,
            Integer pageNumber, Integer pageSize
    ) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("id").descending());
        Page<Comment> commentPage = commentRepository.findByItemId(itemId, pageable);
        Page<CommentDto> commentDtoPage = commentPage.map(CommentDto::fromEntity);
        return commentDtoPage;
    }
    public CommentDto updateComment(Long id, CommentDto dto) {
        Optional<Comment> optionalComment = commentRepository.findById(id);
        Comment comment;
        if (optionalComment.isPresent()) {
            comment = optionalComment.get();
            if (!dto.getPassword().equalsIgnoreCase(comment.getPassword())) {
                return null;
            }
            comment.setWriter(dto.getWriter());
            //comment.setPassword(dto.getPassword());
            comment.setContent(dto.getContent());
            return CommentDto.fromEntity(commentRepository.save(comment));
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public boolean deleteComment(Long itemId, Long commentId, CommentDto dto) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            if (dto.getPassword().equalsIgnoreCase(comment.getPassword()) && itemId.equals(comment.getItemId())) {
                commentRepository.delete(comment);
                return true;
            }
        }
        return false;
    }
    public CommentDto updateReply(CommentDto dto) {
        Long itemId = dto.getItemId();
        Long commentId = dto.getCommentId();

         Comment comment = commentRepository.findById(commentId).orElse(null);
         if (comment != null && comment.getItemId().equals(itemId)) {
             comment.setReply(dto.getReply());
             commentRepository.save(comment);
         }

        return dto;
    }

    public NegotiationDto createNeg(NegotiationDto dto) {
        Negotiation newNeg = new Negotiation();
        newNeg.setItemId(dto.getItemId());
        newNeg.setWriter(dto.getWriter());
        newNeg.setPassword(dto.getPassword());
        newNeg.setSuggestedPrice(dto.getSuggestedPrice());
        newNeg.setStatus("제안");
        return NegotiationDto.fromEntity(negotiationRepository.save(newNeg));
    }

    public Page<NegotiationDto> readNegPage(Long itemId, Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("id"));
        Page<Negotiation> negPage;
        negPage = negotiationRepository.findByItemId(itemId, pageable);

        return negPage.map(NegotiationDto::fromEntity);
    }

    public NegotiationDto updateNeg(Long id, NegotiationDto dto) {
        Optional<Negotiation> optionalNegotiation = negotiationRepository.findById(id);
        Negotiation negotiation;
        if(optionalNegotiation.isPresent()) {
            negotiation = optionalNegotiation.get();
//            if(!dto.getPassword().equals(negotiation.getPassword())) {
//                return null;
//            }
            negotiation.setWriter(dto.getWriter());
            //negotiation.setPassword(dto.getPassword());
            negotiation.setSuggestedPrice(dto.getSuggestedPrice());
            return NegotiationDto.fromEntity(negotiationRepository.save(negotiation));
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
    public NegotiationDto updateNegFromItemWriter(Long itemId, Long id, NegotiationDto dto) {
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        Item item;
        Optional<Negotiation> optionalNegotiation = negotiationRepository.findById(id);
        Negotiation negotiation;
        if(optionalItem.isPresent()) {
            item = optionalItem.get();
            negotiation = optionalNegotiation.get();
            negotiation.setStatus(dto.getStatus());
            return NegotiationDto.fromEntity(negotiationRepository.save(negotiation));
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
    public NegotiationDto updateNegFromNegWriter(Long id, NegotiationDto dto) {
        Optional<Negotiation> optionalNegotiation = negotiationRepository.findById(id);
        Negotiation negotiation;
        if (optionalNegotiation.isPresent()) {
            negotiation = optionalNegotiation.get();
//            if (!dto.getWriter().equals(negotiation.getWriter()) && !dto.getPassword().equals(negotiation.getPassword())) {
//                return null;
//            }
            negotiation.setStatus(dto.getStatus());
            negotiation.setSuggestedPrice(dto.getSuggestedPrice());
            return NegotiationDto.fromEntity(negotiationRepository.save(negotiation));
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
    public boolean deleteNeg(Long id, Long proposalId, NegotiationDto dto) {
        Optional<Negotiation> optionalNegotiation = negotiationRepository.findById(id);
        if(optionalNegotiation.isPresent()) {
            Negotiation negotiation = optionalNegotiation.get();
            if (dto.getPassword().equals(negotiation.getPassword()) && proposalId.equals(negotiation.getId())) {
                negotiationRepository.delete(negotiation);
                return true;
            }
        }
        return false;
    }
}
