package com.NagiGroup.conroller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/load_addition")
public class LoadPriceAddition {
	@PostMapping("/calculateTotal")
    public double calculateTotal(@RequestBody List<Double> amounts) {
        // Calculate the total sum of the amounts
        double total = 0.0;
        for (Double amount : amounts) {
            total += amount;
        }
        return total;
    }
	
	@RestController
    @RequestMapping("/api/percentage")
    public class PercentageController {

        @GetMapping("/of")
        public ResponseEntity<Double> calculatePercentOfValue(
                @RequestParam double percent,
                @RequestParam double value) {

            double result = (percent / 100) * value;
            return ResponseEntity.ok(result);
        }
    }
	
	
	
}
