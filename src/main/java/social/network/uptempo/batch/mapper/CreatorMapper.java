package social.network.uptempo.batch.mapper;

import java.util.List;

import social.network.uptempo.set.data.EData;

public interface CreatorMapper {

	public void creatorAdd(EData eData) throws Exception;
	
	public void creatorContactAdd(EData eData) throws Exception;
	
	public void creatorContactRemove(EData creatorData) throws Exception;

	public void creatorCategoryAdd(EData eData) throws Exception;

	public void creatorCategoryRemove(EData creatorData) throws Exception;

	public List<EData> getContactList(EData pageData) throws Exception;

	public void creatorContactTypeModify(EData contactObject) throws Exception;

	public void modify(EData eData) throws Exception;
}
