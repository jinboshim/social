/**
 * @author jinbos
 * @version 00.00.01
 * @date 2012.11.09
 * @category kr.co.etribe.framework.db
 * @description Mybatis를 통한 DB 와의 Connectiond을 Statement/ResultSet 기능을 위한 Interface
 */
package social.network.uptempo.set.db;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import social.network.uptempo.set.data.EData;


public interface CommonDao {
	
	/**
	 * 데이터 조회 - 1 row
	 * @param statmentName
	 * @param conditions
	 * @return
	 */
	public EData selectItem(String statementName, EData conditions);

	/**
	 * 데이터 조회
	 * @param statmentName
	 * @param conditions
	 * @return
	 */
	public List<Object> selectList(String statementName, EData conditions);
	public List<Object> selectList(String statementName, EData conditions, RowBounds rb);
	
	
	/**
	 * 데이터 조회( Map 형식으로 되며 Value 부분에 Object가 가능하므로 Array 항목이 들어갈수도 있음)
	 * @param statementName mapper id
	 * @param mapKey 결과물에서 특정 Column을 선정하면 Map 형식의 Key로 사용됨
	 * @param conditions
	 * @return
	 */
	public Map<String, Object> selectMap(String statementName, String mapKey, EData conditions);
	
	
	/**
	 * 데이터 입력
	 * @param statementName
	 * @param conditons
	 */
	public void insertItem(String statementName, EData conditions);
	public Object insertItem2(String statementName, Object conditions);
	
	/**
	 * 데이터 수정
	 * @param statementName
	 * @param conditons
	 */
	public int updateItem(String statementName, EData conditions);
	public int updateItem2(String statementName, Object conditions);
	
	/**
	 * 데이터 삭제
	 * @param statmentName
	 * @param conditions
	 */
	public int deleteItem(String statementName, EData conditions);
}
