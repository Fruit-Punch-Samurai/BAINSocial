package tv.bain.bainsocial.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import tv.bain.bainsocial.backend.BAINServer;
import tv.bain.bainsocial.databinding.PostCreateFragmentBinding;
import tv.bain.bainsocial.utils.MyState;
import tv.bain.bainsocial.viewmodels.PostCreateViewModel;

import static android.app.Activity.RESULT_OK;

public class PostCreateFrag extends Fragment {

    private static final int SELECT_PHOTO = 1;

    private PostCreateViewModel vm;
    private PostCreateFragmentBinding b;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return initiateDataBinding(container);
    }

    private View initiateDataBinding(ViewGroup container) {
        b = PostCreateFragmentBinding.inflate(getLayoutInflater(), container, false);
        return b.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        vm = new ViewModelProvider(this).get(PostCreateViewModel.class);
        bindData();
        observeState();
        initButtons();
    }

    public void initButtons(){
        b.openImageSelect.setOnClickListener(v -> {
            BAINServer.getInstance().SendToast("Select Image");
            launchImgSelectionIntent();
        });
        b.setTags.setOnClickListener(v -> BAINServer.getInstance().SendToast("Set Tags"));
    }

    private void bindData() {
        b.setLifecycleOwner(getViewLifecycleOwner());
        b.setViewModel(vm);
        b.setFrag(this);
    }

    private void observeState() {
        vm.getState().observe(getViewLifecycleOwner(), myState -> {
            if (myState instanceof MyState.FINISHED) goBackToHomeFrag();
            else if (myState instanceof MyState.ERROR) {
                Toast.makeText(requireActivity(), ((MyState.ERROR) myState).getMsg(), Toast.LENGTH_SHORT).show();
            }
            vm.setIdleState();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        /* Keeps the bottom side buttons hidden when the keyboard is shown */
        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
    }

    @Override
    public void onDestroyView() {
        b = null;
        super.onDestroyView();
    }

    public void submitPost() {
        vm.submitPost();
    }

    public void goBackToHomeFrag() {
        NavHostFragment.findNavController(this).popBackStack();
    }

////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////

    private void launchImgSelectionIntent() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        Intent chooser = new Intent(Intent.ACTION_CHOOSER);
        chooser.putExtra(Intent.EXTRA_INTENT, photoPickerIntent);
        chooser.putExtra(Intent.EXTRA_TITLE, "Image Selection");

        Intent[] intentArray = {cameraIntent};
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);

        startActivityForResult(chooser, SELECT_PHOTO);
    }

    public void onActivityResult(int reqCode, int resCode, Intent data) {
        if (resCode != RESULT_OK) return;
        if (reqCode == SELECT_PHOTO && data != null) {

            //vm.saveProfileImage(data);
            //Bitmap profileImage = vm.getProfileImage();
            //binding.hvProfileImage.setImageBitmap(profileImage);

            //Drawable finalDrawable = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(profileImage, 50, 50, true));
            //b.toolbar.setNavigationIcon(finalDrawable);
            ImageView newImage = null;
            //newImage.setImageBitmap();
            //b.PostingLayoutScrollView.addView();
        }
    }
}