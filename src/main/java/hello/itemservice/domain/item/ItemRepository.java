package hello.itemservice.domain.item;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ItemRepository {

    private static final Map<Long, Item> store = new HashMap<>();
    private static long sequence = 0L;

    public Item save(Item item) {

        item.setId(++sequence);
        // HashMap에 key = id, value = item 으로 저장
        store.put(item.getId(), item);

        return item;
    }

    public Item findById(long id) {
        return store.get(id);
    }

    public List<Item> findAll() {
        return new ArrayList<>(store.values()); // store.values()로 바로 반환하지 않는 이유는 item을 추가했을때 store에 영향을 주지 않기 위함.
    }

    public void update(Long itemId, Item updateParam) {
        // this.findById 메서드로 저장된 item을 id로 찾아냄
        Item findItem = findById(itemId);
        // 찾은 item의 참조변수에 set으로 넘어온 값으로 변경해줌
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
    }

    public void delete(Long itemId) {
        if (findById(itemId) == null) return;
        store.remove(itemId);
    }

    // test 코드에서 사용하기 위해 작성
    public void clearStore() {
        store.clear();
    }
}
