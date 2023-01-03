package com.enrique.reservatusalaback.controller;

import com.enrique.reservatusalaback.model.Material;
import com.enrique.reservatusalaback.service.MaterialService;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class MaterialControllerTest {

    @MockBean
    private MaterialService materialService;

    @Autowired
    private MockMvc mockMvc;

    private final EasyRandom mockGenerator = new EasyRandom(
            new EasyRandomParameters()
                    .randomizationDepth(2)
                    .collectionSizeRange(0,5)
    );

    @DisplayName("POST add material")
    @Test
    public void whenAddNewMaterial_ThenReturnOkAndMaterialWithId() throws Exception {
        Material material = mockGenerator.nextObject(Material.class);
        Material materialNoId = new Material(
                material.getMaterial(),
                material.getQuantity()
        );
        doReturn(material).when(materialService).add(materialNoId);

        this.mockMvc.perform(post("/material")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(materialNoId)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id", is(material.getId())))
                .andExpect(jsonPath("$.material", is(material.getMaterial())))
                .andExpect(jsonPath("$.quantity", is(material.getQuantity())))
                .andExpect(jsonPath("$.deleted", is(material.isDeleted())));
    }

    @DisplayName("POST add material empty material")
    @Test
    public void whenAddNewMaterialWithNoMaterial_ThenReturnBadRequestAndError() throws Exception {
        Material material = mockGenerator.nextObject(Material.class);
        JSONObject object = new JSONObject();
        object.put("quantity", material.getQuantity());

        this.mockMvc.perform(post("/material")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(object.toString()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is("3")))
                .andExpect(jsonPath("$.description",
                        is("Bad request: Request body has empty or wrong formatted data.")))
                .andExpect(jsonPath("$.material", is("Material is required")));
    }

    @DisplayName("GET all materials")
    @Test
    public void whenFindAllMaterials_ThenReturnOkAndListOfMaterials() throws Exception {
        List<Material> materials = mockGenerator.objects(Material.class, 5).toList();
        doReturn(materials).when(materialService).findAll();

        this.mockMvc.perform(get("/material/findAll"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].id", is(materials.get(0).getId())))
                .andExpect(jsonPath("$[0].material", is(materials.get(0).getMaterial())))
                .andExpect(jsonPath("$[0].quantity", is(materials.get(0).getQuantity())))
                .andExpect(jsonPath("$[0].deleted", is(materials.get(0).isDeleted())))
                .andExpect(jsonPath("$[4].id", is(materials.get(4).getId())))
                .andExpect(jsonPath("$[4].material", is(materials.get(4).getMaterial())))
                .andExpect(jsonPath("$[4].quantity", is(materials.get(4).getQuantity())))
                .andExpect(jsonPath("$[4].deleted", is(materials.get(4).isDeleted())));
    }

    @DisplayName("GET material by valid id")
    @Test
    public void whenFindMaterialByValidId_ThenReturnOkAndMaterialWithThatId() throws Exception {
        Material material = mockGenerator.nextObject(Material.class);
        doReturn(material).when(materialService).findById(material.getId());

        this.mockMvc.perform(get("/material/findById")
                        .param("id", material.getId().toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(material.getId())))
                .andExpect(jsonPath("$.material", is(material.getMaterial())))
                .andExpect(jsonPath("$.quantity", is(material.getQuantity())))
                .andExpect(jsonPath("$.deleted", is(material.isDeleted())));
    }

    @DisplayName("GET material by invalid id")
    @Test
    public void whenFindMaterialByInvalidId_ThenReturnNotFoundAndNotFoundIdError() throws Exception {
        Material material = mockGenerator.nextObject(Material.class);
        doReturn(null).when(materialService).findById(material.getId());

        this.mockMvc.perform(get("/material/findById")
                        .param("id", material.getId().toString()))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(1)))
                .andExpect(jsonPath("$.description",
                        is("Invalid ID: ID not found in database.")));
    }

    @DisplayName("PUT update material valid id")
    @Test
    public void whenUpdateMaterialWithValidId_ThenReturnOkAndUpdatedMaterial() throws Exception {
        Material updatedMaterial = mockGenerator.nextObject(Material.class);
        doReturn(updatedMaterial).when(materialService).update(any(Material.class));

        this.mockMvc.perform(put("/material")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedMaterial)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(updatedMaterial.getId())))
                .andExpect(jsonPath("$.material", is(updatedMaterial.getMaterial())))
                .andExpect(jsonPath("$.quantity", is(updatedMaterial.getQuantity())))
                .andExpect(jsonPath("$.deleted", is(updatedMaterial.isDeleted())));
    }

    @DisplayName("PUT update material invalid id")
    @Test
    public void whenUpdateMaterialWithInvalidId_ThenReturnNotFoundAndNotFoundIdError() throws Exception {
        Material updatedMaterial = mockGenerator.nextObject(Material.class);
        doReturn(null).when(materialService).update(updatedMaterial);

        this.mockMvc.perform(put("/material")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedMaterial)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(1)))
                .andExpect(jsonPath("$.description",
                        is("Invalid ID: ID not found in database.")));
    }

    @DisplayName("DELETE material by valid id")
    @Test
    public void whenDeleteMaterialWithValidId_ThenReturnOk() throws Exception {
        Material material = mockGenerator.nextObject(Material.class);
        doReturn(0).when(materialService).deleteById(material.getId());

        this.mockMvc.perform(delete("/material")
                        .param("id", material.getId().toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(0)))
                .andExpect(jsonPath("$.description", is("OK")));
    }

    @DisplayName("DELETE material by invalid id")
    @Test
    public void whenDeleteMaterialWithInvalidId_ThenReturnNotFound() throws Exception {
        Material material = mockGenerator.nextObject(Material.class);
        doReturn(1).when(materialService).deleteById(material.getId());

        this.mockMvc.perform(delete("/material")
                        .param("id", material.getId().toString()))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(1)))
                .andExpect(jsonPath("$.description",
                        is("Invalid ID: ID not found in database.")));
    }

    static String asJsonString(final Material material) throws JSONException {
        JSONObject object = new JSONObject();
        if (material.getId() != null) {
            object.put("id", material.getId());
        }
        object.put("material", material.getMaterial());
        object.put("quantity", material.getQuantity());

        return object.toString();
    }
}
