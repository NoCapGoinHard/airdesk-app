package it.airdesk.airdesk_app.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;
import it.airdesk.airdesk_app.model.Company;
import it.airdesk.airdesk_app.service.CompanyService;

@Controller
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @GetMapping("/searchCompany")
    @ResponseBody
    public List<Company> searchCompany(@RequestParam("name") String name) {
        return companyService.searchCompanyByName(name);
    }

    @GetMapping("/company-suggestions")
    @ResponseBody
    public List<String> getCompanySuggestions(@RequestParam("query") String query) {
        return companyService.findCompanyNamesLike(query);
    }
}