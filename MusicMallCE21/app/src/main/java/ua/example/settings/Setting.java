package ua.example.settings;


import ua.player.musicmallce.R;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.Window;
import android.widget.Toast;

public class Setting extends PreferenceActivity
{
	@SuppressWarnings("deprecation")
	@Override 
	public void onCreate(Bundle savedInstanceState)
	{
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
	    super.onCreate(savedInstanceState); 
	    
	    // ��������� ������������ �� �������� 
	    addPreferencesFromResource(R.xml.setting);
	    Preference filePicker = (Preference) findPreference(getString(R.string.pref_way));
	    filePicker.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
	    	private String m_chosenDir = "";
           // private boolean m_newFolderEnabled = true;
	        @Override
	        public boolean onPreferenceClick(Preference preference) {
	        	// Create DirectoryChooserDialog and register a callback 
                DirectoryChooserDialog directoryChooserDialog = 
                new DirectoryChooserDialog(Setting.this, 
                    new DirectoryChooserDialog.ChosenDirectoryListener() 
                {
                    @Override
                    public void onChosenDir(String chosenDir) 
                    {
                        m_chosenDir = chosenDir;
                        Toast.makeText(Setting.this, "Директория: " + chosenDir, Toast.LENGTH_LONG).show();
                        setDir(chosenDir);
                    }
                }); 
                // Toggle new folder button enabling
              //  directoryChooserDialog.setNewFolderEnabled(m_newFolderEnabled);
                // Load directory chooser dialog for initial 'm_chosenDir' directory.
                // The registered callback will be called upon final directory selection.
                directoryChooserDialog.chooseDirectory(m_chosenDir);
             //   m_newFolderEnabled = ! m_newFolderEnabled;
	            return true;
	        }
	    });
	    
	}
	
	void setDir(String dir) {
	    //get the new value from Intent data
	  
	    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this); ;
	    SharedPreferences.Editor editor = preferences.edit();
	    editor.putString(getString(R.string.pref_way), dir);
	    editor.commit();
	}
}