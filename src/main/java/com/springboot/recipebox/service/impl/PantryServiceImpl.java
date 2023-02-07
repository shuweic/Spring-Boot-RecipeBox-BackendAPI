package com.springboot.recipebox.service.impl;

import com.springboot.recipebox.entity.Pantry;
import com.springboot.recipebox.entity.Pantry.PantryKey;
import com.springboot.recipebox.entity.User;
import com.springboot.recipebox.payload.IngredientDTO;
import com.springboot.recipebox.repository.PantryRepository;
import com.springboot.recipebox.repository.UserRepository;
import com.springboot.recipebox.service.PantryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PantryServiceImpl implements PantryService {

    private PantryRepository pantryRepository;

    private UserRepository userRepository;

    @Autowired
    public PantryServiceImpl(PantryRepository pantryRepository, UserRepository userRepository)
    {
        this.pantryRepository = pantryRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void addIngredientToUserPantry(String username, String ingredientName)
    {
        try
        {
            // crate pantry key
            PantryKey pantryKey = new PantryKey();

            // get user
            Optional<User> user = userRepository.findUserByUsername(username);

            // set pantry key fields
            pantryKey.setUser(user.get());
            pantryKey.setIngredientName(ingredientName);

            // create pantry row and set key
            Pantry pantry = new Pantry();
            pantry.setPantryKey(pantryKey);

            // save to data table
            pantryRepository.save(pantry);
        }
        catch (Exception e)
        {
            throw e;
        }
    }

    @Override
    public void removeIngredientFromUserPantry(String username, String ingredientName)
    {
        Pantry pantryRow = pantryRepository.findByPantryKeyUserUsernameAndPantryKeyIngredientName(username, ingredientName);
        if (pantryRow != null)
        {
            pantryRepository.delete(pantryRow);
        }
    }

    @Override
    public List<String> getAllIngredientNamesByUsername(String username)
    {
        List<String> ingredientNamesList = new ArrayList<>();

        // get all pantry rows by username from database
        List<Pantry> pantryList = pantryRepository.findAllByPantryKeyUserUsername(username);

        // add ingredient name of the pantry row into the return list
        pantryList.forEach(x -> ingredientNamesList.add(x.getPantryKey().getIngredientName()));

        return ingredientNamesList;
    }
}
