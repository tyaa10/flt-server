package org.tyaa.training.current.server.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.stereotype.Service;

@Service
public abstract class BaseService {

    private final ObjectMapper objectMapper;

    protected BaseService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    protected <T> T applyJsonPatch(JsonPatch patch, T targetModel) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(objectMapper.convertValue(targetModel, JsonNode.class));
        return (T) objectMapper.treeToValue(patched, targetModel.getClass());
    }
}
