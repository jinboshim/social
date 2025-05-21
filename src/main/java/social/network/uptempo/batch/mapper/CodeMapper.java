package social.network.uptempo.batch.mapper;

import java.util.List;

import social.network.uptempo.set.data.EData;

public interface CodeMapper {

	public void categoryAdd(EData eData) throws Exception;
	
	public List<EData> tiktokCategoryList(EData eData) throws Exception;
	
	public List<EData> tiktokCategoryBeautyList(EData eData) throws Exception;
	
	public List<EData> tiktokCategoryBeautyListUS(EData eData) throws Exception;
}
