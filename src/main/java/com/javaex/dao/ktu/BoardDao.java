package com.javaex.dao.ktu;

import java.util.List;
import java.util.Vector;

import com.javaex.vo.ktu.BoardVo;


public interface BoardDao {

	public List<BoardVo> getList();  // 게시물 전체 목록 조회
	public BoardVo getBoard(int no); // 게시물 상세 조회
	public int insert(BoardVo vo);   // 게시물 등록
	public int delete(int no);       // 게시물 삭제
	public int update(BoardVo vo);   // 게시물 수정
	public int getTotalCount(String keyField, String keyWord); //총 게시물수
	public  Vector<BoardVo> getBoardList(String keyField, String keyWord, int start, int end);
	public void upcount(int no);


}
