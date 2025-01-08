package it.airdesk.airdesk_app.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.airdesk.airdesk_app.model.Facility;
import it.airdesk.airdesk_app.repository.FacilityRepository;

@Service
public class FacilityService {
        
    @Autowired
    private FacilityRepository facilityRepository;

    public Facility findById(Long id) {
        return facilityRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Facility not found with the ID:" + id));
    }

    public List<Facility> findAllByCity(String city) {
        return facilityRepository.findByBuildingCityContainingIgnoreCase(city);
    }

    public Map<Long, Integer> findWorkstationCountByFacility() { //not invoked, but you can use it to have the overall amount of workstations in a facility
        List<Object[]> results = facilityRepository.findWorkstationCountByFacility();
        Map<Long, Integer> facility2workstations = new HashMap<>();
        for (Object[] result : results) {
            Long facilityId = (Long) result[0];
            Integer workstations = ((Long) result[1]).intValue();
            facility2workstations.put(facilityId, workstations);
        }
        return facility2workstations;
    }

    // Fetch all facilities for a given company
    public List<Facility> findByCompanyId(Long companyId) {
        return facilityRepository.findByCompanyId(companyId);
    }

    public void save(Facility facility) {
        this.facilityRepository.save(facility);
    }

    public void deleteById(Long id) {
        this.facilityRepository.deleteById(id);
    }

	public boolean alreadyExists(Facility facility) {
		List<Facility> facilities = this.facilityRepository.findByName(facility.getEmail());
		if (facilities.size() > 0)
			return true;
		else 
			return false;
	}

}
