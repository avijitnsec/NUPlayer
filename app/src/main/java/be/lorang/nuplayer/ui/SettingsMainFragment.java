/*
 * Copyright 2021 Geert Lorang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package be.lorang.nuplayer.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;


import be.lorang.nuplayer.R;

/*
 * Fragment that manages the Settings (sub)menu
 */

public class SettingsMainFragment extends HorizontalMenuFragment {

    private static final String TAG = "SettingsMainFragment";
    private static final String[] menuItems = {"Settings", "Token status", "About"};

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayout menuNavigationContainer = view.findViewById(R.id.menuNavigationContainer);
        LinearLayout menuContentContainer = view.findViewById(R.id.menuContentContainer);

        for(int i=0;i<menuItems.length;i++) {

            Button button = createMenuButton(menuItems[i]);
            button.setNextFocusUpId(R.id.buttonTopNavSettings);

            button.setOnFocusChangeListener((v, hasFocus) -> {

                if(hasFocus) {

                    setSelectedMenuButton(button);

                    // Fragment options
                    String buttonText = button.getText().toString();
                    switch(buttonText) {

                        case "Settings":
                            if(!(getSelectedFragment() instanceof SettingsFragment)) {
                                setSelectedFragment(new SettingsFragment());
                            }
                            break;
                        case "Token status":
                            if(!(getSelectedFragment() instanceof TokenStatusFragment)) {
                                setSelectedFragment(new TokenStatusFragment());
                            }
                            break;
                        case "About":
                            if(!(getSelectedFragment() instanceof AboutFragment)) {
                                setSelectedFragment(new AboutFragment());
                            }
                            break;
                        default:
                            setSelectedFragment(null);
                            break;
                    }

                    if(menuContentContainer != null) {
                        loadFragment(R.id.menuContentContainer);
                    }
                }
            });

            // Set button ids
            switch(menuItems[i]) {
                case "Settings":
                    button.setId(R.id.buttonSubSettingsSettings);
                    break;
                case "Token status":
                    button.setId(R.id.buttonSubSettingsTokenStatus);
                    break;
                case "About":
                    button.setId(R.id.buttonSubSettingsAbout);
                    break;
            }

            if(menuNavigationContainer != null) {
                menuNavigationContainer.addView(button);
            }
        }

        // add default Fragment
        if(menuContentContainer != null) {
            setSelectedFragment(new SettingsFragment());
            loadFragment(R.id.menuContentContainer);
        }


    }

}