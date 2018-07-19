package cloud.cinder.web.addressbook.controller;

import cloud.cinder.web.address.service.AddressService;
import cloud.cinder.web.addressbook.controller.model.NewContactModel;
import cloud.cinder.web.addressbook.controller.vo.ContactVO;
import cloud.cinder.web.addressbook.service.AddressBookService;
import cloud.cinder.web.coinmarketcap.dto.Currency;
import cloud.cinder.web.coinmarketcap.service.PriceService;
import cloud.cinder.web.wallet.service.AuthenticationService;
import cloud.cinder.ethereum.util.EthUtil;
import lombok.extern.slf4j.Slf4j;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.Locale;
import java.util.stream.Collectors;

import static cloud.cinder.ethereum.util.EthUtil.format;

@Controller
@RequestMapping("/wallet/address-book")
@Slf4j
public class AddressBookController {

    private final AuthenticationService authenticationService;
    private final AddressBookService addressBookService;
    private PriceService priceService;
    private AddressService addressService;

    private DecimalFormat decimalFormat = new DecimalFormat("0.00");
    private final static PrettyTime prettytime = new PrettyTime(Locale.ENGLISH);


    public AddressBookController(final AuthenticationService authenticationService,
                                 final AddressBookService addressBookService,
                                 final PriceService priceService,
                                 final AddressService addressService) {
        this.authenticationService = authenticationService;
        this.addressBookService = addressBookService;
        this.priceService = priceService;
        this.addressService = addressService;
    }

    @GetMapping
    public String index(final ModelMap modelMap) {
        authenticationService.requireAuthenticated();
        final String address = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        modelMap.put("address", address);
        modelMap.put("contacts", addressBookService.getContacts(authenticationService.getAddress())
                .stream()
                .map(x -> {
                    final BigInteger bal = addressService.getBalance(x.getAddress()).toBlocking().first();
                    return ContactVO.builder()
                            .address(x.getAddress())
                            .nickname(x.getNickname())
                            .balance(format(bal))
                            .lastModified(prettytime.format(x.getLastModified()))
                            .balEur(getBalance(bal, Currency.EUR))
                            .balUsd(getBalance(bal, Currency.USD))
                            .id(x.getId())
                            .build();
                })
                .collect(Collectors.toList())
        );
        modelMap.put("newContactModel", new NewContactModel());
        return "wallets/address-book";
    }

    private String getBalance(final BigInteger bal, final Currency eur) {
        return decimalFormat.format(priceService.getPrice(eur) * EthUtil.asEth(bal));
    }

    @PostMapping("/new")
    public String newContact(@ModelAttribute("newContactModel") @Valid final NewContactModel newContactModel,
                             final BindingResult bindingResult,
                             final ModelMap modelMap,
                             final RedirectAttributes redirectAttributes) {
        authenticationService.requireAuthenticated();
        if (bindingResult.hasErrors()) {
            return index(modelMap);
        } else {
            try {
                addressBookService.addContact(authenticationService.getAddress(), newContactModel.getAddress(), newContactModel.getNickname());
                redirectAttributes.addAttribute("success", "Successfully added your new contact");
                return "redirect:/wallet/address-book";
            } catch (final Exception ex) {
                modelMap.put("error", ex.getMessage());
                return index(modelMap);
            }
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteContact(@PathVariable("id") final Long id) {
        authenticationService.requireAuthenticated();
        try {
            addressBookService.deleteContact(authenticationService.getAddress(), id);
        } catch (final Exception ex) {
            log.error("Something went wrong while trying to delete contact", ex);
        }
        return "redirect:/wallet/address-book";
    }
}
