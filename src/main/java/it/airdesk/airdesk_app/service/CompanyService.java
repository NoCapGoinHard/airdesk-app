package it.airdesk.airdesk_app.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.airdesk.airdesk_app.model.Company;
import it.airdesk.airdesk_app.repository.CompanyRepository;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    public void save(Company company) {
        this.companyRepository.save(company);
    }

    public List<Company> searchCompanyByName(String name) {
        return companyRepository.findByNameLike(name);
    }

    public List<String> findCompanyNamesLike(String query) { //search engine behavior for inserting a company name
        return companyRepository.findByNameLike(query)
                .stream()
                .map(company -> company.getName())  // Extract the names
                .collect(Collectors.toList());
    }

    // Find an existing company by name or create a new one
    public Company findOrCreateCompanyByName(String companyName) { //if the company exists it returns the company, otherwise it gets added to the DB
        return companyRepository.findByName(companyName)
            .orElseGet(() -> {
                // Create a new company if not found
                Company newCompany = new Company(companyName);
                return companyRepository.save(newCompany);
            });
    }

    public Optional<Company> findByName(String name) {
        return this.companyRepository.findByName(name);
    }
}
