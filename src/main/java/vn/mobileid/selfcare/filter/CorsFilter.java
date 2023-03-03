//package vn.mobileid.selfcare.filter;
//
//import java.io.IOException;
//import javax.servlet.Filter;
//import javax.servlet.FilterChain;
//import javax.servlet.FilterConfig;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.annotation.WebFilter;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//public class CorsFilter implements Filter {
//    
//    public CorsFilter() {}
//    
//    public void destroy() {
//        // TODO Auto-generated method stub
//    }
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
//         HttpServletRequest request = (HttpServletRequest) servletRequest;
//        System.out.println("CORSFilter HTTP Request: " + request.getMethod());
// 
//        // Authorize (allow) all domains to consume the content
//        ((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Origin", "https://ajax.googleapis.com, https://momentjs.com");
//        ((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Methods","GET, POST");
// 
//        HttpServletResponse resp = (HttpServletResponse) servletResponse;
// 
//        // For HTTP OPTIONS verb/method reply with ACCEPTED status code -- per CORS handshake
//        if (request.getMethod().equals("OPTIONS")) {
//            resp.setStatus(HttpServletResponse.SC_ACCEPTED);
//            return;
//        }
// 
//        // pass the request along the filter chain
//        chain.doFilter(request, servletResponse);
//    }
//
//    
//}
