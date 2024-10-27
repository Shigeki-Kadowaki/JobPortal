package com.jobportal.JobPortal.Controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.groups.ConvertGroup;
import lombok.Data;

import java.util.List;

@Data
public class validateTest {
//    @NotBlank
//    @NotNull
//    private String defaultText;
//    @NotBlank(message = "text1blank", groups = {atext.class})
//    @NotNull(message = "text1null", groups = {atext.class})
//    private String text1;
//    @NotBlank(message = "text2blank", groups = {btext.class})
//    @NotNull(message = "text2null", groups = {btext.class})
//    private String text2;
    @NotEmpty(message = "parent list1 error")
    @Valid @ConvertGroup.List({@ConvertGroup(from=ValidationGroup.atext.class, to= ValidationGroup.atext.class), @ConvertGroup(from=ValidationGroup.btext.class, to= ValidationGroup.btext.class)})
    private List<@Valid @ConvertGroup.List({@ConvertGroup(from=ValidationGroup.atext.class, to= ValidationGroup.atext.class), @ConvertGroup(from=ValidationGroup.btext.class, to= ValidationGroup.btext.class)}) validateTestChild> list1;
}
