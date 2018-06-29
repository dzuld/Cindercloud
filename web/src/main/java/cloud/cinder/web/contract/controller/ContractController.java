package cloud.cinder.web.contract.controller;

import cloud.cinder.ethereum.abi.AbiDecoder;
import cloud.cinder.ethereum.abi.domain.AbiContractFunction;
import cloud.cinder.web.address.service.AddressService;
import cloud.cinder.web.contract.controller.command.AccessContractCommand;
import cloud.cinder.web.contract.controller.command.GenerateUICommand;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/contract")
public class ContractController {

    private AbiDecoder abiDecoder;
    private AddressService addressService;

    public ContractController(final AbiDecoder abiDecoder,
                              final AddressService addressService) {
        this.abiDecoder = abiDecoder;
        this.addressService = addressService;
    }

    @GetMapping
    public String index(final ModelMap modelMap) {
        modelMap.putIfAbsent("accessContractCommand", new AccessContractCommand());
        return "contracts/access";
    }

    @PostMapping
    public String accessContract(@ModelAttribute("accessContractCommand") @Valid final AccessContractCommand accessContractCommand,
                                 final BindingResult bindingResult,
                                 final ModelMap modelMap) {
        if (bindingResult.hasErrors()) {
            return index(modelMap);
        } else {
            if (!addressService.isContract(accessContractCommand.getAddress())) {
                modelMap.put("error", "The provided address is not a contract");
            } else {
                try {
                    modelMap.addAttribute("abiContract", abiDecoder.decode(accessContractCommand.getAbi().trim())
                            .getElements()
                            .stream()
                            .filter(x -> x instanceof AbiContractFunction).map(x -> (AbiContractFunction) x)
                            .filter(AbiContractFunction::isConstant)
                            .collect(Collectors.toList())
                    );
                } catch (final Exception ex) {
                    modelMap.put("error", "We were not able to decode the provided ABI");
                }
            }
            return index(modelMap);
        }
    }

    @PostMapping("/generate-ui")
    public String generateUIForElement(@RequestBody final GenerateUICommand abi,
                                       final ModelMap modelMap) {
        modelMap.put("element", abi.getElements()
                .stream()
                .filter(x -> x instanceof AbiContractFunction)
                .map(x -> (AbiContractFunction) x)
                .filter(x -> x.getName().equalsIgnoreCase(abi.getElementName()))
                .findFirst().orElse(null)
        );
        return "components/contract :: generate-ui";
    }
}
