package poly.edu.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import poly.edu.entity.Enum.BookingStatus;

class ConsultationServiceTest {

    private final ConsultationService service = new ConsultationService();

    @Test
    void featuredPackagesOnlyContainActiveItemsAndRespectLimit() {
        var featured = service.findFeaturedPackages(3);

        assertEquals(3, featured.size());
        assertTrue(featured.stream().allMatch(p -> Boolean.TRUE.equals(p.getActive())));
    }

    @Test
    void mostBookedPackageComesFirst() {
        var featured = service.findFeaturedPackages(3);

        assertEquals(2, featured.get(0).getId());
    }

    @Test
    void featuredConsultantsExcludeInactiveOnes() {
        var featured = service.findFeaturedConsultants(10);

        assertTrue(featured.stream().allMatch(c -> Boolean.TRUE.equals(c.getActive())));
        assertTrue(featured.stream().noneMatch(c -> c.getId() == 5));
    }

    @Test
    void searchByKeywordIgnoresCase() {
        var result = service.searchByKeyword("  CV  ");

        assertEquals(1, result.size());
        assertEquals(3, result.get(0).getId());
    }

    @Test
    void priceRangeIsInclusive() {
        var result = service.findByPriceRange(350000.0, 400000.0);

        assertEquals(2, result.size());
    }

    @Test
    void saveBookingIncreasesCount() {
        var form = new poly.edu.dto.BookingForm();
        form.setConsultationPackageId(1);
        form.setConsultantId(4);
        form.setFullName("Nguyễn Văn A");

        var saved = service.saveBooking(form, null, null);

        assertEquals(18, service.countBookings());
        assertEquals(BookingStatus.NEW, saved.getStatus());
    }

    @Test
    void countsMatchSeedData() {
        assertEquals(6, service.countActivePackages());
        assertEquals(17, service.countBookings());
    }
}
