package social.network.uptempo.batch.mapper;

import social.network.uptempo.set.data.EData;

public interface ScheduleInfoMapper {

	public EData getInfo(EData eData) throws Exception;

	public void modifyNextCount(EData eData) throws Exception;

}
