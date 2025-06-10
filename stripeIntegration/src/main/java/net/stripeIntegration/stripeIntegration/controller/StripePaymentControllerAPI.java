package net.stripeIntegration.stripeIntegration.controller;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.CustomerCollection;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.CustomerListParams;
import net.stripeIntegration.stripeIntegration.model.CustomerData;
import net.stripeIntegration.stripeIntegration.util.StripeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api")
public class StripePaymentControllerAPI {
    @Value("${stripe.apikey}")
    String stripeKey;

    @Autowired
    StripeUtil stripeUtil;
    @RequestMapping("/createCustomer")
    public CustomerData index(@RequestBody CustomerData data) throws StripeException {
        Stripe.apiKey = stripeKey;
        CustomerCreateParams params =
                CustomerCreateParams.builder()
                        .setName(data.getName())
                        .setEmail(data.getEmail())
                        .build();
        Customer customer = Customer.create(params);
        data.setCustomerId(customer.getId());
        return data;
    }
    @RequestMapping("/getAllCustomer")
    public List<CustomerData> getAllCustomer() throws StripeException {
        Stripe.apiKey = stripeKey;

        CustomerListParams params = CustomerListParams.builder().build();

        CustomerCollection customers = Customer.list(params);
        List<CustomerData> allCustomer = new ArrayList<CustomerData>();
        for(int i=0; i< customers.getData().size();i++){
            CustomerData customerData = new CustomerData();
            customerData.setCustomerId(customers.getData().get(i).getId());
            customerData.setName(customers.getData().get(i).getName());
            customerData.setEmail(customers.getData().get(i).getEmail());
            allCustomer.add(customerData);

        }
        return allCustomer;
    }

    @RequestMapping("/deleteCustomer/{id}")
    public String deleteCustomer(@PathVariable("id") String id) throws StripeException {
        Stripe.apiKey = stripeKey;

        Customer resource = Customer.retrieve(id);

        Customer customer = resource.delete();
        return "successfully deleted";
    }

    @RequestMapping("/getCustomer/{id}")
    public CustomerData getCustomer(@PathVariable("id") String id) throws StripeException {

        Stripe.apiKey = stripeKey;

        CustomerData output = stripeUtil.getCustomer(id);
        return output;
    }
}
