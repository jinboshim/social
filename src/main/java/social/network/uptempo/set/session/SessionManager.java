package social.network.uptempo.set.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionManager{
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
    public final static String SESSION_NAME_ADMIN = "admin";    
    public final static String SESSION_NAME_MENU_LIST = "mlist";
	public static final String SESSION_NAME_PROGRAM_AUTH_TYPE = "program_auth_type";
    

    /**
     * 관리자 세션 정보에서 아이디 정보를 가져온다.
     * @param request
     * @return
     */
    public static String getId(HttpServletRequest request){
        String id = getAdmin(request).getEmail();
        return id;
    }
    
    /**
     * 관리자 세션 정보에서 회원 타입 정보를 가져온다.
     * @param request
     * @return
     */
    public static int getType(HttpServletRequest request){
    	int type = getAdmin(request).getType();
    	return type;
    }
    
    /**
     * 관리자 세션 정보에서 관리자이름 정보를 가져온다.
     * @param request
     * @return
     */
    public static String getName(HttpServletRequest request){
    	String name = getAdmin(request).getName();
    	return name;
    }
    
    /**
     * 관리자 세션 정보를 가져온다.
     * @param request
     * @return
     */
    public static Admin getAdmin(HttpServletRequest request){
        Admin admin = (Admin)request.getSession().getAttribute(SESSION_NAME_ADMIN);
        return admin;
    }
    
//    /**
//     * 관리자 세션 정보를 저장한다.
//     * @param request
//     * @return
//     */
//    public static void setAdmin(HttpServletRequest request, Admin admin){
//    	HttpSession session = request.getSession(true);
//    	
//    	session.setAttribute(SESSION_NAME_ADMIN, admin);
//    	
//    	//session timeout : 60min
//    	session.setMaxInactiveInterval(60 * 60);
//    }
    

    /**
     * 관리자 로그인 여부
     * @param request
     * @return
     */
    public static boolean isAdminLogined(HttpServletRequest request){
        if(getAdmin(request) == null){
            return false;
        }else{
            return true;
        }
    }
    

    /**
     * 세션정보와 쿠키정보를 모두 삭제한다.
     * @param request
     * @param response
     */
    public static void sessionInvalidate(HttpServletRequest request, HttpServletResponse response){
        request.getSession(false).invalidate();
        
        response.setHeader("Set-Cookie", "");
    }     
}
