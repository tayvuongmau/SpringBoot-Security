package springsecurity.multi_language;

import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

public class PreferVietnameseAcceptHeaderLocaleResolver extends AcceptHeaderLocaleResolver {

    public PreferVietnameseAcceptHeaderLocaleResolver() {
        setDefaultLocale(Locale.forLanguageTag("vi"));
    }

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        String acceptLanguage = request.getHeader("Accept-Language");
        if (acceptLanguage == null || !acceptLanguage.contains("vi")) {
            return super.resolveLocale(request);
        } else {
            return Locale.forLanguageTag("vi");
        }
    }
}
