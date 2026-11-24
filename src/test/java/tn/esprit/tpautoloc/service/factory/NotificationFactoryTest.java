package tn.esprit.tpautoloc.service.factory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tn.esprit.tpautoloc.service.observer.EmailNotificationObserver;
import tn.esprit.tpautoloc.service.observer.SmsNotificationObserver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NotificationFactoryTest {

    private EmailNotificationObserver email;
    private SmsNotificationObserver sms;
    private NotificationFactory factory;

    @BeforeEach
    void setUp() {
        email = new EmailNotificationObserver();
        sms = new SmsNotificationObserver();
        factory = new NotificationFactory(email, sms);
    }

    @Test
    void doitRetournerEmailObserver() {
        assertThat(factory.creerObservateur("EMAIL")).isSameAs(email);
    }

    @Test
    void doitRetournerSmsObserver() {
        assertThat(factory.creerObservateur("sms")).isSameAs(sms);
    }

    @Test
    void doitRejeterUnCanalInconnu() {
        assertThatThrownBy(() -> factory.creerObservateur("FAX"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
