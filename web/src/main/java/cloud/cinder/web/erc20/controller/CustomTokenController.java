package cloud.cinder.web.erc20.controller;

import cloud.cinder.web.erc20.controller.model.NewCustomTokenModel;
import cloud.cinder.web.erc20.service.CustomERC20Service;
import cloud.cinder.web.wallet.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/wallet/custom-tokens")
public class CustomTokenController {

    @Autowired
    private CustomERC20Service customERC20Service;
    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping
    public String index(final ModelMap modelMap) {
        authenticationService.requireAuthenticated();
        modelMap.put("customTokens", customERC20Service.findAll(authenticationService.getAddress()));
        modelMap.putIfAbsent("newCustomTokenModel", new NewCustomTokenModel());
        return "wallets/custom-tokens";
    }

    @PostMapping("/new")
    public String newToken(@ModelAttribute("newCustomTokenModel") @Valid final NewCustomTokenModel newCustomTokenModel,
                             final BindingResult bindingResult,
                             final ModelMap modelMap,
                             final RedirectAttributes redirectAttributes) {
        authenticationService.requireAuthenticated();
        if (bindingResult.hasErrors()) {
            return index(modelMap);
        } else {
            try {
                if (customERC20Service.add(newCustomTokenModel.getAddress(), authenticationService.getAddress())) {
                    redirectAttributes.addFlashAttribute("success", "Successfully added your new token");
                    return "redirect:/wallet/custom-tokens";
                } else {
                    modelMap.put("error", newCustomTokenModel.getAddress() + " was not recognized as a valid erc20 token");
                    return index(modelMap);
                }
            } catch (final Exception ex) {
                modelMap.put("error", ex.getMessage());
                return index(modelMap);
            }
        }
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") final Long id) {
        customERC20Service.delete(id, authenticationService.getAddress());
        return "redirect:/wallet/custom-tokens";
    }
}
