//package springsecurity.multi_language;
//
//import org.springframework.context.MessageSource;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.support.ReloadableResourceBundleMessageSource;
//import org.springframework.context.support.ResourceBundleMessageSource;
//import org.springframework.web.servlet.LocaleResolver;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//import org.springframework.web.servlet.i18n.CookieLocaleResolver;
//import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
//import org.springframework.web.servlet.i18n.SessionLocaleResolver;
//
//import java.util.Locale;
//
//@Configuration
//public class LanguageConfig implements WebMvcConfigurer {
//
//    // Cấu hình LocaleResolver
//    // Cấu hình file LocaleResolver để ứng dụng có thể biết được locale (tiếng Mỹ , tiếng Việt hay tiếng Pháp)
//    // mà ứng dụng đang sử dụng là gì ?
//    @Bean
//    public LocaleResolver localeResolver(){
//        //sử dụng Session để chuyển đổi qua lại các ngôn ngữ ( cũng có thể chọn cookie)
//        CookieLocaleResolver resolver= new CookieLocaleResolver();
//        resolver.setCookieDomain("myAppLocaleCookie");
//        resolver.setDefaultLocale(Locale.ENGLISH);
//        // 60 minutes
//        resolver.setCookieMaxAge(60*60);
//        return resolver;
//    }
//
//    //Tiếp theo , chúng ta cần thêm một Interceptor có nhiệm vụ chuyển đổi qua lại
//    // giữa các locale mới khi giá trị thay đổi.
////    @Bean
////    public LocaleChangeInterceptor localeChangeInterceptor(){
////        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
////        //đặt tên 1 tham số để sau dùng làm gốc ngôn ngữ cần đổi
////        lci.setParamName("lang");
////        return lci;
////    }
//
//    //Method addInterceptors sẽ xác định thay đổi ngôn ngữ hiển thị thông qua param name nào trên trình duyệt.
//    // Ví dụ trên URL có param: ?language=en thì tiếng anh sẽ được hiển thị
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        LocaleChangeInterceptor localeInterceptor = new LocaleChangeInterceptor();
//        localeInterceptor.setParamName("language");
//        registry.addInterceptor(localeInterceptor).addPathPatterns("/*");
//    }
//
//    @Bean(name = "messageSource")
//    public MessageSource getMessageResource()  {
//        ReloadableResourceBundleMessageSource messageResource= new ReloadableResourceBundleMessageSource();
//
//        // Đọc vào file i18n/messages_xxx.properties
//        // Ví dụ: i18n/messages_en.properties
//        messageResource.setBasename("classpath:i18n/messages");
//        messageResource.setDefaultEncoding("UTF-8");
//        return messageResource;
//    }
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//}
