package springsecurity.multi_language;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@Configuration
public class LocaleConfig implements WebMvcConfigurer {
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        //đặt ngôn ngữ tiếng việt làm mặc định cho project khi khởi chạy
        slr.setDefaultLocale(new Locale("vi","VN"));
        return slr;
    }

//  Tiếp theo , chúng ta cần thêm một Interceptor có nhiệm vụ chuyển đổi qua lại
//  giữa các locale mới khi giá trị thay đổi.
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor(){
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        //đặt tên 1 tham số để sau dùng làm gốc ngôn ngữ cần đổi
        lci.setParamName("language");
        return lci;
    }

    //Method addInterceptors sẽ xác định thay đổi ngôn ngữ hiển thị thông qua param name nào trên trình duyệt.
    // Ví dụ trên URL có param: ?language=en thì tiếng anh sẽ được hiển thị
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        // Đọc vào file i18n/messages_xxx.properties
        // Ví dụ: i18n/messages_en.properties
        messageSource.setBasename("classpath:/i18n/message");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

}