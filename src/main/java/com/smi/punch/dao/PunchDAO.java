package com.smi.punch.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Repository;

import com.smi.punch.IContent;
import com.smi.punch.vo.PunchRecordVO;


public class PunchDAO {
	private static final Logger logger = LoggerFactory.getLogger(PunchDAO.class);
	/*
	


	private JdbcTemplate jdbcTemplate;

	public PunchRecordVO selectLastRecord(String empNo, String sDate, String eDate, String type) {
		List params = new ArrayList<Object>();
		String sql = "SELECT * FROM punch_data WHERE status='NORMAL' ";
		sql += " AND emp_no=? ";
		params.add(empNo);
		sql += " AND punch_datetime >= ?";
		params.add(sDate);
		sql += " AND punch_datetime <= ?";
		params.add(eDate);
		sql += " AND punch_type = ?";
		params.add(type);
		sql += " ORDER BY cdatetime DESC LIMIT 1";
		logger.info("[selectLastRecord]sql={},params={}",sql, params.toArray());
		List<PunchRecordVO> voList = jdbcTemplate.query(sql, params.toArray(), new PunchRecordMap());
		return voList.isEmpty()?null:voList.get(0);
	}
	public List<PunchRecordVO> select(String empNo, String sDate, String eDate) {
		String sql = "SELECT * FROM punch_data WHERE status='NORMAL' ";
		List params = new ArrayList<Object>();

		if (StringUtils.isNotBlank(empNo)) {
			sql += " AND emp_no=?";
			params.add(empNo);
		}
		if (StringUtils.isNotBlank(sDate)) {
			sql += " AND punch_datetime >= ?";
			params.add(sDate);
		}
		if (StringUtils.isNotBlank(eDate)) {
			sql += " AND punch_datetime <= ?";
			params.add(eDate);
		}
		sql += " ORDER BY punch_datetime DESC ";
		logger.info("[select]sql={}",sql);
		List<PunchRecordVO> voList = jdbcTemplate.query(sql, params.toArray(), new PunchRecordMap());
		return voList;
	}
	
	public List<PunchRecordVO> selectPunchTime(String empNo, String sDate, String eDate) {
		String sql = "SELECT * FROM punch_data WHERE status='NORMAL' AND (punch_type='punch_in' or punch_type='punch_out') ";
		List params = new ArrayList<Object>();

		if (StringUtils.isNotBlank(empNo)) {
			sql += " AND emp_no=?";
			params.add(empNo);
		}
		if (StringUtils.isNotBlank(sDate)) {
			sql += " AND punch_datetime >= ?";
			params.add(sDate);
		}
		if (StringUtils.isNotBlank(eDate)) {
			sql += " AND punch_datetime <= ?";
			params.add(eDate);
		}
		sql += " ORDER BY punch_datetime DESC ";
		logger.info("[selectPunchTime]sql={}",sql);
		List<PunchRecordVO> voList = jdbcTemplate.query(sql, params.toArray(), new PunchRecordMap());
		return voList;
	}

	public boolean insert(PunchRecordVO vo) {
		String sql = "INSERT INTO punch_data (dept_no, dept_name, emp_no, name, punch_datetime, punch_type, other_desc) VALUES (?,?,?,?,?,?,?)";
		logger.info("[insert]sql={}",sql);
		int result = jdbcTemplate.update(sql, new Object[] { vo.getDeptNo(), vo.getDeptName(), vo.getEmpNo(),
				vo.getName(), new Timestamp(vo.getPunchDateTime()), vo.getPunchType() , vo.getOtherDesc()});
		return result > 0;
	}
	
	public boolean delete(Integer id, String empNo) {
		String sql = "UPDATE punch_data set STATUS='DELETE' WHERE ID=? AND EMP_NO=?";
		logger.info("sql={}",sql);
		int result = jdbcTemplate.update(sql, new Object[] { id, empNo});
		return result > 0;
	}
	
	public List<PunchRecordVO> selectDuplicateRecord(String empNo, String calDate) {
		String sql = "SELECT * FROM punch_data WHERE status='NORMAL'   ";
		List params = new ArrayList<Object>();

		if (StringUtils.isNotBlank(empNo)) {
			sql += " AND emp_no=? ";
			params.add(empNo);
		}
		if (StringUtils.isNotBlank(calDate)) {
			sql += " and date(punch_datetime)=? ";
			params.add(calDate);
		}
		logger.info("[selectDuplicateRecord] sql={}",sql);
		List<PunchRecordVO> voList = jdbcTemplate.query(sql, params.toArray(), new PunchRecordMap());
		return voList;
	}
	public class PunchRecordMap implements RowMapper<PunchRecordVO> {

		@Override
		public PunchRecordVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			PunchRecordVO bean = new PunchRecordVO();
			bean.setId(rs.getInt("id"));
			bean.setDeptNo(rs.getString("dept_no"));
			bean.setDeptName(rs.getString("dept_name"));
			bean.setEmpNo(rs.getString("emp_no"));
			bean.setName(rs.getString("name"));
			bean.setPunchType(rs.getString("punch_type"));
			bean.setCreateDateTime(rs.getTimestamp("cdatetime").getTime());
			bean.setPunchDateTime(rs.getTimestamp("punch_datetime").getTime());
			bean.setOtherDesc(rs.getString("other_desc"));
			bean.setStatus(rs.getString("status"));
			if(IContent._act_others.equals(rs.getString("punch_type"))) {
				bean.setPunchTypeName("Others");
			}else if(IContent._act_punch_in.equals(rs.getString("punch_type"))) {
				bean.setPunchTypeName("Punch In");
			}else if(IContent._act_punch_out.equals(rs.getString("punch_type"))) {
				bean.setPunchTypeName("Punch Out");
			}else if(IContent._act_travel.equals(rs.getString("punch_type"))) {
				bean.setPunchTypeName("Biz Trip");
			}else if(IContent._act_traning.equals(rs.getString("punch_type"))) {
				bean.setPunchTypeName("Traning");
			}else if(IContent._act_take_off.equals(rs.getString("punch_type"))) {
				bean.setPunchTypeName("Leave");
			}else {
				bean.setPunchTypeName("NA");
			}
			return bean;
		}
	}
	*/
}
