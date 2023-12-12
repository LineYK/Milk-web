package com.milk.milkweb.service;


import com.milk.milkweb.entity.Board;
import com.milk.milkweb.entity.BoardHashTag;
import com.milk.milkweb.entity.HashTag;
import com.milk.milkweb.repository.BoardHashTagRepository;
import com.milk.milkweb.repository.HashTagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class HashTagService {

	private final HashTagRepository hashTagRepository;
	private final BoardHashTagRepository boardHashTagRepository;


	public void saveHashTag(Set<String> dtoTags, Board board) {
		if (dtoTags.isEmpty()) return;

		List<HashTag> hashTags = dtoTags.stream()
				.filter(s -> !hashTagRepository.existsByTag(s))
				.map(HashTag::of)
				.toList();
		hashTagRepository.saveAll(hashTags);

		List<BoardHashTag> boardHashTags = dtoTags.stream()
				.map(hashTagRepository::findByTag)
				.filter(Optional::isPresent)
				.map(Optional::get)
				.map(tag -> BoardHashTag.of(board, tag))
				.toList();
		boardHashTagRepository.saveAll(boardHashTags);
	}


}
