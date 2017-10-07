package aor.demo.crud.utils;

import aor.demo.crud.repos.AORSpecifications;
import aor.demo.crud.repos.GenericRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ApiUtils {

    public <T> Page<T> filterByHelper(GenericRepository<T> repo, AORSpecifications<T> specifications, String filterStr, String rangeStr, String sortStr) {
        JSONObject filter = null;
        if (filterStr != null) {
            filter = JSON.toJsonObject(filterStr);
        }
        JSONArray range = null;
        if (rangeStr != null) {
            range = JSON.toJsonArray(rangeStr);
        }
        int page = 0;
        int size = Integer.MAX_VALUE;
        if (range != null) {
            page = (Integer) range.get(0) - 1;
            size = (Integer) range.get(1);
        }

        JSONArray sort = null;
        if (sortStr != null) {
            sort = JSON.toJsonArray(sortStr);
        }
        String sortBy = "id";
        String order = "DESC";
        if (range != null) {
            sortBy = (String) sort.get(0);
            order = (String) sort.get(1);
        }

        Sort.Direction sortDir = Sort.Direction.DESC;
        if (order.equals("ASC")) {
            sortDir = Sort.Direction.ASC;
        }

        if (filter == null || filter.length() == 0) {
            return repo.findAll(new PageRequest(page, size, sortDir, sortBy));
        }
        else if (filter.has("id")) {
            List idsList = (ArrayList) filter.toMap().get("id");
            return repo.findByIdIn(makeListsInteger(idsList), new PageRequest(page, size, sortDir, sortBy));
        }
        else if (filter.has("q")) {
            String text = (String) filter.get("q");
            return repo.findAll(Specifications.where(specifications.seachInAllAttributes(text)), new PageRequest(page,size, sortDir, sortBy));
        }
        else {
            HashMap<String,Object> map = (HashMap<String,Object>) filter.toMap();
            return repo.findAll(Specifications.where(specifications.equalToEachColumn(map)), new PageRequest(page,size, sortDir, sortBy));
        }
    }

    public List<Integer> makeListsInteger(List list) {
        if (!list.isEmpty() && list.get(0) instanceof Integer) {
            return (List<Integer>) list;
        }
        else if (!list.isEmpty() && list.get(0) instanceof String) {
            List<Integer> intList = new ArrayList<>();
            for (Object o : list) {
                intList.add(Integer.parseInt((String)o));
            }
            return intList;
        }
        else {
            return new ArrayList<>();
        }
    }
}
