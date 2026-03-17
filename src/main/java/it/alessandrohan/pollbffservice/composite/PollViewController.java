package it.alessandrohan.pollbffservice.composite;

import it.alessandrohan.pollbffservice.composite.dto.response.PollViewResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bff/api/v0/polls")
public class PollViewController {
    private final PollViewService pollViewService;

    public PollViewController(PollViewService pollViewService) {
        this.pollViewService = pollViewService;
    }

    @GetMapping("/{id}/view")
    @ResponseStatus(HttpStatus.OK)
    public PollViewResponse getPollView(
            @PathVariable("id") Long id,
            @RequestHeader("Authorization") String authorization
    ) {
        return pollViewService.getPollView(id, authorization);
    }
}
