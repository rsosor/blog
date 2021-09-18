package com.rsosor.app.controller.admin.api;

import com.rsosor.app.annotation.DisableOnCondition;
import com.rsosor.app.model.dto.OptionDTO;
import com.rsosor.app.model.dto.OptionSimpleDTO;
import com.rsosor.app.model.entity.Option;
import com.rsosor.app.model.params.OptionParam;
import com.rsosor.app.model.params.OptionQuery;
import com.rsosor.app.service.IOptionService;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import static org.springframework.data.domain.Sort.Direction.DESC;

/**
 * OptionController
 *
 * @author RsosoR
 * @date 2021/9/19
 */
@RestController
@RequestMapping("/api/admin/options")
public class OptionController {

    private final IOptionService optionService;

    public OptionController(IOptionService optionService) {
        this.optionService = optionService;
    }

    @GetMapping
    @ApiOperation("Lists options")
    public List<OptionDTO> listAll() {
        return optionService.listDtos();
    }

    @PostMapping("saving")
    @ApiOperation("Saves options")
    @DisableOnCondition
    public void saveOptions(@Valid @RequestBody List<OptionParam> optionParams) {
        optionService.save(optionParams);
    }

    @GetMapping("map_view")
    @ApiOperation("Lists all options with map view")
    public Map<String, Object> listAllWithMapView() {
        return optionService.listOptions();
    }

    @PostMapping("map_view/keys")
    @ApiOperation("Lists options with map view by keys")
    public Map<String, Object> listAllWithMapView(@RequestBody List<String> keys) {
        return optionService.listOptions(keys);
    }

    @GetMapping("list_view")
    @ApiOperation("Lists all options with list view")
    public Page<OptionSimpleDTO> listAllWithListView(
            @PageableDefault(sort = "updateTime", direction = DESC) Pageable pageable,
            OptionQuery optionQuery) {
        return optionService.pageDtosBy(pageable, optionQuery);
    }

    @GetMapping("{id:\\d+}")
    @ApiOperation("Gets option detail by id")
    public OptionSimpleDTO getBy(@PathVariable("id") Integer id) {
        Option option = optionService.getById(id);
        return optionService.convertToDto(option);
    }

    @PostMapping
    @ApiOperation("Creates option")
    @DisableOnCondition
    public void createBy(@RequestBody @Valid OptionParam optionParam) {
        optionService.save(optionParam);
    }

    @PutMapping("{optionId:\\d+}")
    @ApiOperation("Updates option")
    @DisableOnCondition
    public void updateBy(@PathVariable("optionId") Integer optionId,
                         @RequestBody @Valid OptionParam optionParam) {
        optionService.update(optionId, optionParam);
    }

    @DeleteMapping("{optionId:\\d+}")
    @ApiOperation("Deletes option")
    @DisableOnCondition
    public void deletePermanently(@PathVariable("optionId") Integer optionId) {
        optionService.removePermanently(optionId);
    }

    @PostMapping("map_view/saving")
    @ApiOperation("Saves options by option map")
    @DisableOnCondition
    public void saveOptionWithMapView(@RequestBody Map<String, Object> optionMap) {
        optionService.save(optionMap);
    }
}
