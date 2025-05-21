package social.network.uptempo.constants;

public class MessageConstants {
	             
	

	
	
//	############## Result Code ##############
//	# 정상일 경우
//	success=000:success
	public static final String SUCCESS					= "success";

//	# 공통 오류일 경우
//	error=999:알수 없는 에러가 발생하였습니다.\n잠시후에 다시 시도해 주세요.
//	error.db=888:DB에서 오류가 발생하였습니다.\n잠시후에 다시 시도해 주세요.
//	error.null=777:DB에서 오류가 발생하였습니다.\n잠시후에 다시 시도해 주세요.
	public static final String ERROR					= "error";
	public static final String ERROR_DB					= "error.db";
	public static final String ERROR_NULL				= "error.null";

	
	
//	# 로그인시 오류일 경우(200) : access
//	fail.access.param=200:필수파라미터가 입력되지 않았습니다.({0})
	public static final String FAIL_ACCESS_PARAM		= "fail.access.param";
//	fail.access.param=210:사용자가 없습니다.
	public static final String FAIL_ACCESS_NO_USER		= "fail.access.no.user";
//	fail.access.param=220:비밀번호가 맞지 않습니다.
	public static final String FAIL_ACCESS_NOT_PASSWORD		= "fail.access.not.password";
//	fail.access.param=230:중복 로그인 되어 있습니다.
	public static final String FAIL_ACCESS_DUPLICATE		= "fail.access.duplicate";

	
	
	
	
//	# 등록시 오류일 경우(300) : create
//	fail.insert.param=300:필수파라미터가 입력되지 않았습니다.({0})
	public static final String FAIL_INSERT_PARAM		= "fail.insert.param";
	public static final String FAIL_INSERT_VALID		= "fail.insert.valid";

//	# 조회시 오류일 경우(400) : read
//	fail.read.param=400:필수파라미터가 입력되지 않았습니다.({0})
	public static final String FAIL_READ_PARAM			= "fail.read.param";

//	# 수정시 오류일 경우(500) : update
//	fail.update.param=500:필수파라미터가 입력되지 않았습니다.({0})
	public static final String FAIL_UPDATE_PARAM		= "fail.update.param";
	public static final String FAIL_UPDATE_VALID		= "fail.update.valid";
	
//	# 삭제시 오류일 경우(600) : delete
//	fail.delete.param=600:필수파라미터가 입력되지 않았습니다.({0})
	public static final String FAIL_DELETE_PARAM		= "fail.delete.param";


	public static final String FAIL_SESSION_INFO		= "fail.session.info";
	public static final String FAIL_PERMISSION_INFO		= "fail.permission.info";
	
	
	public static final String FAIL_SEARCH_EMPTY		= "fail.search.empty";
	
}
