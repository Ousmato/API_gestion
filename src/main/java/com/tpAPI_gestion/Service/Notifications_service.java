package com.tpAPI_gestion.Service;

import com.tpAPI_gestion.Entity.Budget;
import com.tpAPI_gestion.Entity.Depenses;
import com.tpAPI_gestion.Entity.Notifications;
import com.tpAPI_gestion.Entity.User;
import com.tpAPI_gestion.Repository.Budget_repository;
import com.tpAPI_gestion.Repository.Category_repository;

import com.tpAPI_gestion.Repository.Notification_repository;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.springframework.data.relational.core.sql.render.SqlRenderer.create;

@Service
public class Notifications_service {

    @Autowired
    private Notification_repository notificationRepository;


    @Autowired
    private Budget_repository budgetRepository;

    @Autowired
    private Category_repository categoryRepository;

    @Value("${twilio.accountSid}")
    private String twilioAccountSid;

    @Value("${twilio.authToken}")
    private String twilioAuthToken;

    @Value("${twilio.fromPhoneNumber}")
    private String twilioFromPhoneNumber;



    public boolean alerte(Notifications notifications, Depenses depenses) {

        Budget budget = notifications.getBudget();
        if (budget==null){
            throw new RuntimeException("Budget does not exist");
        }

       // Depenses depenses1 = depenseRepository.findById(depenses.getId());
        int montant = budget.getReliquat()-depenses.getAmount();
        if (montant <= budget.getSeuil()) {

            notifications.setText("Atteintion votre budget est en bat du seuil qui est "+budget.getSeuil());

            User categoryUser = categoryRepository.findById(budget.getCategory().getId()).getUser();
            Notifications categoryNotifier = new Notifications();
            categoryNotifier.setText("Atteintion votre budget pour la "+budget.getCategory().getType()+" est en bas du seuil");
            categoryNotifier.setUser(categoryUser);
            categoryNotifier.setBudget(budget);
            notifications.setDate(LocalDate.now());
            notifications.setHeure(LocalTime.now());
           notificationRepository.save(notifications);

            // Envoi d'une alerte par SMS
            //String recipientPhoneNumber = "+22373855156";
           // String message = notifications.getText();
            //sendSms(recipientPhoneNumber, message);
           return true;
        }else
            return false;
    }

   /* private void sendSms(String recipientPhoneNumber, String message) {
        Twilio.init(twilioAccountSid, twilioAuthToken);
        Message.creator(
                        new PhoneNumber(recipientPhoneNumber),
                        new PhoneNumber(twilioFromPhoneNumber),
                        message)
                .create();
    }*/




    //methode Get
    public List<Notifications> read(){
        return notificationRepository.findAll();
    }

    //methode Delete
    public String delete(int id){

        notificationRepository.deleteById(id);
        return "delete successful";
    }

    //methode Put
    public Notifications update(Notifications notifications){
        Notifications notifications1 = notificationRepository.save(notifications);
        if (notifications1 !=null){
            return notifications1;
        }else throw new EntityExistsException("this rapport exist");
    }
}
