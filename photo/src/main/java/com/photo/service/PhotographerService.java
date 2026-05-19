package com.photo.service;

import com.photo.model.PhotographerDTO;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class PhotographerService {
    // Keep data in the root folder so it aligns with previous execution
    private static final String FILE_NAME = "photographers.txt";

    public List<PhotographerDTO> getAll() {
        List<PhotographerDTO> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 7) {
                    PhotographerDTO dto = new PhotographerDTO();
                    dto.setType(parts[0]);
                    dto.setId(parts[1]);
                    dto.setName(parts[2]);
                    dto.setContact(parts[3]);
                    dto.setExp(Integer.parseInt(parts[4]));
                    dto.setPrice(Double.parseDouble(parts[5]));
                    dto.setSpecialty(parts[6]);
                    list.add(dto);
                }
            }
        } catch (IOException e) {
            // file may not exist
        }
        return list;
    }

    public void add(PhotographerDTO dto) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            bw.write(toCsvLine(dto));
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update(String id, PhotographerDTO dto) {
        List<PhotographerDTO> all = getAll();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (PhotographerDTO p : all) {
                if (p.getId().equals(id)) {
                    bw.write(toCsvLine(dto));
                } else {
                    bw.write(toCsvLine(p));
                }
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void delete(String id) {
        List<PhotographerDTO> all = getAll();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (PhotographerDTO p : all) {
                if (!p.getId().equals(id)) {
                    bw.write(toCsvLine(p));
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String toCsvLine(PhotographerDTO dto) {
        return dto.getType() + "," + dto.getId() + "," + dto.getName() + "," + dto.getContact() + "," + dto.getExp() + "," + dto.getPrice() + "," + dto.getSpecialty();
    }
}
