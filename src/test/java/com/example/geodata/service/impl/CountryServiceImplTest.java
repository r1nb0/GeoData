package com.example.geodata.service.impl;

import com.example.geodata.cache.LRUCacheCountry;
import com.example.geodata.dto.CountryDTO;
import com.example.geodata.entity.Country;
import com.example.geodata.exceptions.BadRequestException;
import com.example.geodata.exceptions.ResourceNotFoundException;
import com.example.geodata.repository.CountryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CountryServiceImplTest {

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private LRUCacheCountry countryCache;

    @InjectMocks
    private CountryServiceImpl countryService;

    @Test
    void getAll() {

    }

    @Test
    void findCountryById_invalidId() {
        int id = 1;

        when(countryCache.get(id)).thenReturn(Optional.empty());
        when(countryRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> countryService.findById(id));
    }

    @Test
    void findCountryById_existingIdAndCountryNotInCache()
            throws ResourceNotFoundException {
        int id = 1;
        Optional<Country> expectedCountry = Optional.of(new Country());

        when(countryCache.get(id)).thenReturn(Optional.empty());
        when(countryRepository.findById(id)).thenReturn(expectedCountry);

        Optional<Country> actualCountry = countryService.findById(id);
        assertEquals(expectedCountry, actualCountry);
        verify(countryCache, times(1))
                .put(id, expectedCountry.get());
    }

    @Test
    void findCountryById_existingIdAndCountryInCache()
            throws ResourceNotFoundException {
        int id = 1;
        Optional<Country> expectedCountry = Optional.of(new Country());

        when(countryCache.get(id)).thenReturn(expectedCountry);

        Optional<Country> actualCountry = countryService.findById(id);

        assertEquals(expectedCountry, actualCountry);
        verify(countryRepository, never()).findById(anyInt());
    }

    @Test
    void createCountry_existingCountry() {
        CountryDTO countryDTO = CountryDTO.builder()
                .name("Japan")
                .build();

        when(countryRepository.existsByName(countryDTO.name()))
                .thenReturn(true);

        assertThrows(BadRequestException.class,
                () -> countryService.createCountry(countryDTO));
    }

    @Test
    void createCountry_illegalArguments() {
        CountryDTO countryDTO = CountryDTO.builder()
                .name("Japan")
                .build();

        when(countryRepository.existsByName(countryDTO.name()))
                .thenReturn(false);

        assertThrows(BadRequestException.class,
                () -> countryService.createCountry(countryDTO));
    }

    @Test
    void createCountry_success() {
        CountryDTO countryDTO = CountryDTO.builder()
                .name("Japan")
                .latitude(13.5677)
                .longitude(15.3322)
                .nationality("Japanese")
                .build();

        when(countryRepository.existsByName(countryDTO.name()))
                .thenReturn(false);

        Country createdCountry = countryService
                .createCountry(countryDTO);
        assertEquals(createdCountry.getName(), countryDTO.name());
        assertEquals(createdCountry.getNationality(), countryDTO.nationality());
        assertEquals(createdCountry.getLatitude(), countryDTO.latitude());
        assertEquals(createdCountry.getLongitude(), countryDTO.longitude());
        verify(countryRepository, times(1))
                .save(any(Country.class));
    }

    @Test
    void deleteCountryById_invalidId() {
        int id = 1;

        when(countryRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> countryService.deleteById(id));
    }

    @Test
    void deleteCountryById_existingId()
            throws ResourceNotFoundException {
        int id = 1;
        Optional<Country> expectedCountry = Optional.of(new Country());

        when(countryRepository.findById(id))
                .thenReturn(expectedCountry);

        countryService.deleteById(id);

        verify(countryRepository, times(1))
                .deleteById(anyInt());
    }

    @Test
    void addLanguage() {

    }

    @Test
    void deleteLanguage() {

    }

    @Test
    void updateInfo_invalidId() {
        CountryDTO countryDTO = CountryDTO.builder()
                .id(1)
                .build();

        when(countryRepository.findById(countryDTO.id()))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> countryService.updateInfo(countryDTO));
    }

    @Test
    void updateInfo_existingId()
            throws ResourceNotFoundException {
        CountryDTO countryDTO = CountryDTO.builder()
                .id(1)
                .name("Japan")
                .nationality("Japanese")
                .latitude(43.1234)
                .longitude(12.2323)
                .build();
        Optional<Country> expectedCountry = Optional.of(new Country());

        when(countryRepository.findById(countryDTO.id()))
                .thenReturn(expectedCountry);
        when(countryRepository.existsByName(countryDTO.name()))
                .thenReturn(false);

        Country updatedCountry = countryService
                .updateInfo(countryDTO);

        assertEquals(updatedCountry.getName(), countryDTO.name());
        assertEquals(updatedCountry.getNationality(), countryDTO.nationality());
        assertEquals(updatedCountry.getLatitude(), countryDTO.latitude());
        assertEquals(updatedCountry.getLongitude(), countryDTO.longitude());
        verify(countryRepository, times(1))
                .save(any(Country.class));
    }

    @Test
    void updateCountry_replaceForExistingName() {
        CountryDTO countryDTO = CountryDTO.builder()
                .id(1)
                .name("Japan")
                .build();
        Optional<Country> expectedCountry = Optional.of(new Country());

        when(countryRepository.findById(countryDTO.id()))
                .thenReturn(expectedCountry);
        when(countryRepository.existsByName(countryDTO.name()))
                .thenReturn(true);

        assertThrows(BadRequestException.class,
                () -> countryService.updateInfo(countryDTO));
    }

    @Test
    void findCountriesWithSpecifiedLanguage() {

    }

    @Test
    void bulkInsert() {

    }
}