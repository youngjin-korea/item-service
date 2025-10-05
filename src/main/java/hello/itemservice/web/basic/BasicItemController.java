package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RequestMapping("/basic/items")
@RequiredArgsConstructor // final 이붙은 멤버변수로 생성자를 만들어줌
@Controller
public class BasicItemController {

    private final ItemRepository itemRepository;

    /**
     * 기능: 목록조회 화면, 화면: templates/basic/items.html
     */
    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);

        return "basic/items"; // @Controller 에 String을 반환하면 ViewResolver가 논리경로 -> 물리적 경로로 페이지 응답
    }

    /**
     * 기능: 상품상세 화면, 화면: tesmplates/basic/item.html
     */
    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);

        return "basic/item";
    }

    /**
     * 기능: 상품등록 화면, 화면: templates/basic/addForm.html
     */
    @GetMapping("/add")
    public String addForm() {
        return "basic/addForm.html";
    }

    /**
     * 기능: 상품등록 기능, 화면: templates/basic/addForm.html
     */
    //@PostMapping("/add")
    public String addItemV1(@RequestParam String itemName,
                            @RequestParam int price,
                            @RequestParam Integer quantity,
                            Model model) {
        Item item = new Item(itemName, price, quantity);
        itemRepository.save(item);
        model.addAttribute("item", item);
        return "basic/item";
    }

    //@PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item) {
        // @ModelAttribute가 Item 객체를 생성해서 set으로 값을 채워줌
        itemRepository.save(item);
        // model.addAttribute("item", item); //@ModelAttribute("item")로 하면 item이라는 명으로 model에 추가해줌
//         return "basic/item"; // 저장하고 브라우저에서 새로고침을 하면 마지막으로 요청한 요청이 한번 더 보내지므로 무한 Post요청이 들어옴 해결책은 PRG 패턴
        return "redirect:/basic/items/" + item.getId();
    }

    @PostMapping("/add")
    public String addItemV6(Item item, RedirectAttributes redirectAttributes) {
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true); // 경로로 넣어주지 않은 요소는 쿼리파라미터 형식으로 붙음, url encoding도 해줌
        return "redirect:/basic/items/{itemId}";
    }

    /**
     * 기능: 상품수정 기능, 화면: templates/basic/editForm.html
     */
    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);

        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/basic/items/{itemId}"; // 상품상세 조회 컨트롤러 호출함
    }

    @ResponseBody
    @DeleteMapping("/{itemId}/edit")
    public ResponseEntity<Void> delete(@PathVariable Long itemId) {
        itemRepository.delete(itemId);
        return ResponseEntity.ok().build(); // 상품목록 컨트롤러 호출함
    }

    /**
     * test용 데이터
     */
    @PostConstruct
    public void init() {
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));
    }
}
