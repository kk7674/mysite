package com.javaex.dao.ktu;

import java.util.List;

import com.javaex.vo.ktu.GuestbookVo;

public interface GuestbookDao {
  
	public List<GuestbookVo> getList();

	public int insert(GuestbookVo vo);

	public int delete(GuestbookVo vo);

}
