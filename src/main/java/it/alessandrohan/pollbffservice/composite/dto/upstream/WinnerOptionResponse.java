package it.alessandrohan.pollbffservice.composite.dto.upstream;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WinnerOptionResponse {
    private Long pollId;
    private Long optionId;
    private BigDecimal percentOfWiner;
}
