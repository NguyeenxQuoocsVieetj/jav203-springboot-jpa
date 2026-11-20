package poly.edu.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import poly.edu.dto.BookingForm;
import poly.edu.entity.Booking;
import poly.edu.entity.Enum.BookingStatus;
import poly.edu.entity.Consultant;
import poly.edu.entity.ConsultationCategory;
import poly.edu.entity.ConsultationPackage;

@Service
public class ConsultationService {

    private final List<ConsultationCategory> categories = new ArrayList<>();
    private final List<ConsultationPackage> packages = new ArrayList<>();
    private final List<Consultant> consultants = new ArrayList<>();
    private final List<Booking> bookings = new ArrayList<>();

    public ConsultationService() {
        seedData();
    }

    public List<ConsultationCategory> findCategories() {
        return List.copyOf(categories);
    }

    public Map<String, String> findCategoryNames() {
        return categories.stream()
                .collect(Collectors.toMap(ConsultationCategory::getId, ConsultationCategory::getName));
    }

    public Map<Integer, String> findPackageNames() {
        return packages.stream()
                .collect(Collectors.toMap(ConsultationPackage::getId, ConsultationPackage::getName));
    }

    public Map<Integer, String> findConsultantNames() {
        return consultants.stream()
                .collect(Collectors.toMap(Consultant::getId, Consultant::getFullName));
    }

    public List<ConsultationPackage> findActivePackages() {
        return packages.stream()
                .filter(ConsultationPackage::getActive)
                .toList();
    }

    public List<Consultant> findActiveConsultants() {
        return consultants.stream()
                .filter(Consultant::getActive)
                .toList();
    }

    public List<ConsultationPackage> searchByKeyword(String keyword) {
        String normalized = keyword.trim().toLowerCase(Locale.ROOT);
        return findActivePackages().stream()
                .filter(p -> p.getName().toLowerCase(Locale.ROOT).contains(normalized))
                .toList();
    }

    public List<ConsultationPackage> findByCategory(String categoryId) {
        return findActivePackages().stream()
                .filter(p -> categoryId.equals(p.getCategoryId()))
                .toList();
    }

    public List<ConsultationPackage> findByPriceRange(Double min, Double max) {
        return findActivePackages().stream()
                .filter(p -> min == null || p.getPrice() >= min)
                .filter(p -> max == null || p.getPrice() <= max)
                .toList();
    }

    public Optional<ConsultationPackage> findPackageById(Integer id) {
        return packages.stream()
                .filter(ConsultationPackage::getActive)
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    public List<ConsultationPackage> findRelatedPackages(String categoryId, Integer excludeId, int limit) {
        return findByCategory(categoryId).stream()
                .filter(p -> !p.getId().equals(excludeId))
                .limit(limit)
                .toList();
    }

    public Optional<Consultant> findConsultantById(Integer id) {
        return consultants.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }

    public List<ConsultationPackage> findFeaturedPackages(int limit) {
        return packages.stream()
                .filter(ConsultationPackage::getActive)
                .sorted(Comparator.comparingLong((ConsultationPackage p) -> countBookingsOfPackage(p.getId()))
                        .reversed()
                        .thenComparing(ConsultationPackage::getId))
                .limit(limit)
                .toList();
    }

    public List<Consultant> findFeaturedConsultants(int limit) {
        return consultants.stream()
                .filter(Consultant::getActive)
                .sorted(Comparator.comparingLong((Consultant c) -> countBookingsOfConsultant(c.getId()))
                        .reversed()
                        .thenComparing(Consultant::getId))
                .limit(limit)
                .toList();
    }

    public long countActivePackages() {
        return findActivePackages().size();
    }

    public long countBookings() {
        return bookings.size();
    }

    public Booking saveBooking(BookingForm form, String attachmentFileName, String attachmentOriginalName) {
        Booking booking = Booking.builder()
                .id(bookings.size() + 1)
                .packageId(form.getConsultationPackageId())
                .consultantId(form.getConsultantId())
                .fullName(form.getFullName())
                .email(form.getEmail())
                .phone(form.getPhone())
                .preferredDate(form.getPreferredDate())
                .preferredTime(form.getPreferredTime())
                .note(form.getNote())
                .status(BookingStatus.NEW)
                .attachmentFileName(attachmentFileName)
                .attachmentOriginalName(attachmentOriginalName)
                .build();
        bookings.add(booking);
        return booking;
    }

    public List<String> findTimeSlots() {
        return List.of("08:00 - 10:00", "10:00 - 12:00", "13:30 - 15:30", "15:30 - 17:30");
    }

    public boolean isActivePackage(Integer id) {
        return findPackageById(id).isPresent();
    }

    public boolean isActiveConsultant(Integer id) {
        return findActiveConsultants().stream().anyMatch(c -> c.getId().equals(id));
    }

    public List<Booking> findAllBookings() {
        return List.copyOf(bookings);
    }

    private long countBookingsOfPackage(Integer packageId) {
        return bookings.stream().filter(b -> packageId.equals(b.getPackageId())).count();
    }

    private long countBookingsOfConsultant(Integer consultantId) {
        return bookings.stream().filter(b -> consultantId.equals(b.getConsultantId())).count();
    }

    private void seedData() {
        categories.add(new ConsultationCategory("STUDY", "Tư vấn học tập"));
        categories.add(new ConsultationCategory("CAREER", "Định hướng nghề nghiệp"));
        categories.add(new ConsultationCategory("SKILL", "Kỹ năng nghề nghiệp"));

        consultants.add(Consultant.builder().id(1).fullName("Nguyễn Minh Anh")
                .specialty("Định hướng nghề nghiệp").active(true).build());
        consultants.add(Consultant.builder().id(2).fullName("Trần Quốc Huy")
                .specialty("Lập trình và Công nghệ thông tin").active(true).build());
        consultants.add(Consultant.builder().id(3).fullName("Lê Thu Hà")
                .specialty("Kỹ năng phỏng vấn và CV").active(true).build());
        consultants.add(Consultant.builder().id(4).fullName("Phạm Đức Long")
                .specialty("Lộ trình học tập").active(true).build());
        consultants.add(Consultant.builder().id(5).fullName("Vũ Hải Yến")
                .specialty("Tạm nghỉ").active(false).build());

        packages.add(pkg(1, "Tư vấn chọn ngành học", "STUDY", 300000.0, 60,
                "Phân tích sở thích và năng lực để chọn ngành phù hợp.", 4,
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRChFUHAQZ62vKVwUDb4HJ2gVLDUmrA424n8Imr01zBGQ&s=10"));
        packages.add(pkg(2, "Định hướng nghề IT", "CAREER", 500000.0, 90,
                "Lộ trình nghề nghiệp cho sinh viên ngành công nghệ thông tin.", 2,
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcShu8fdbh9rPM4U4lllRVkO-uM9Zlbz23VDyQ5nEnD4IQ&s=10"));
        packages.add(pkg(3, "Chuẩn bị CV và phỏng vấn", "SKILL", 400000.0, 60,
                "Viết CV ấn tượng và luyện phỏng vấn thực tế.", 3,
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQZg92HiMyf3O5E28sTpcvdAIYx-24Uo6OEALVITdo0Vw&s=10"));
        packages.add(pkg(4, "Lập lộ trình học Lập trình Web", "STUDY", 600000.0, 120,
                "Xây dựng kế hoạch học Java, Spring và Web theo từng giai đoạn.", 2,
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQv_ihV9wtJO3FXPtd037VOqiWKpp2YwuMC41RYiHU0ig&s=10"));
        packages.add(pkg(5, "Xây dựng thương hiệu cá nhân", "SKILL", 350000.0, 60,
                "Tạo hồ sơ LinkedIn và định vị bản thân với nhà tuyển dụng.", 1,
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSQ1kL2b8DAAhs-nNk8UMpKSWEGZJa4UnDfzuVRxoG7IA&s=10"));
        packages.add(pkg(6, "Định hướng du học", "CAREER", 800000.0, 90,
                "Tư vấn chương trình, học bổng và chuẩn bị hồ sơ du học.", 1,
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ15pZZx5CVpAcOjxZorjg3NnlqKfaiAJIkFyKfsYsNKw&s=10"));
        packages.add(pkg(7, "Gói cũ đã ngừng", "CAREER", 200000.0, 45,
                "Gói đã ngừng cung cấp, dùng để kiểm tra lọc trạng thái.", 5, null));
        packages.get(6).setActive(false);

        addBookings(2, 5);
        addBookings(3, 4);
        addBookings(5, 3);
        addBookings(1, 2);
        addBookings(4, 2);
        addBookings(6, 1);
    }

    private ConsultationPackage pkg(int id, String name, String categoryId, double price,
                                    int duration, String description, int consultantId, String image) {
        return ConsultationPackage.builder()
                .id(id).name(name).categoryId(categoryId).price(price).duration(duration)
                .description(description).active(true).consultantId(consultantId).image(image).build();
    }

    private void addBookings(int packageId, int times) {
        Integer consultantId = packages.stream()
                .filter(p -> p.getId() == packageId)
                .findFirst()
                .orElseThrow()
                .getConsultantId();
        for (int i = 0; i < times; i++) {
            int id = bookings.size() + 1;
            bookings.add(Booking.builder()
                    .id(id)
                    .packageId(packageId)
                    .consultantId(consultantId)
                    .fullName("Sinh viên mẫu " + id)
                    .email("sinhvien" + id + "@example.com")
                    .phone("0900000" + String.format("%03d", id))
                    .preferredDate(LocalDate.of(2026, 11, 20).plusDays(id))
                    .preferredTime("08:00 - 10:00")
                    .status(BookingStatus.values()[id % BookingStatus.values().length])
                    .build());
        }
    }
}
