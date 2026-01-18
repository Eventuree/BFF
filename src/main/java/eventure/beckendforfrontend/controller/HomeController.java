package eventure.beckendforfrontend.controller;

import eventure.beckendforfrontend.model.dto.CategoryDto;
import eventure.beckendforfrontend.model.dto.EventDto;
import eventure.beckendforfrontend.model.dto.HomePageDTO;
import eventure.beckendforfrontend.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
public class HomeController {

    private final EventService eventService;

    @GetMapping("/api/home")
    public ResponseEntity<HomePageDTO> getHomePageData(@PathVariable Long id) {
        CompletableFuture<List<EventDto>> trendingFuture =
                CompletableFuture.supplyAsync(() -> eventService.getTrendingEvents());

        CompletableFuture<List<CategoryDto>> categoriesFuture =
                CompletableFuture.supplyAsync(() -> eventService.getCategories());


        CompletableFuture<List<EventDto>> allEventsFuture =
                CompletableFuture.supplyAsync(() -> eventService.getAllEvents());

        CompletableFuture.allOf(trendingFuture, categoriesFuture, allEventsFuture).join();

        HomePageDTO response = new HomePageDTO(
                trendingFuture.join(),
                categoriesFuture.join(),
                allEventsFuture.join()
        );

        return ResponseEntity.ok(response);
    }

}
