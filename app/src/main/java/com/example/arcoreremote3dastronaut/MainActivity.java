package com.example.arcoreremote3dastronaut;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.assets.RenderableSource;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.BaseArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

public class MainActivity extends AppCompatActivity {



    private ArFragment arFragment;
    private ModelRenderable modelRenderable;
    private String Model_URL="https://modelviewer.dev/shared-assets/models/Astronaut.glb";

    //private String Model_URL="https://github.com/dzubov/ARfiles/blob/main/Cube.glb?raw=true";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        setUpModel();
    }

    void setUpModel(){
        ModelRenderable.builder()
                .setSource(this,
                        RenderableSource.builder().setSource(
                                this,
                                Uri.parse(Model_URL),
                                RenderableSource.SourceType.GLB) // Here, we decided to use GLB file
                                .setScale(0.5f) // Here, we scale 3D model
                                .setRecenterMode(RenderableSource.RecenterMode.ROOT)
                                .build()
                ) .
                setRegistryId(Model_URL)
                .build()
                .thenAccept(renderable -> modelRenderable = renderable)
                .exceptionally(throwable -> { // Here, we display an error if any
                    Log.i("Model","cant load");
                    Toast.makeText(MainActivity.this,"Model can't be Loaded",
                            Toast.LENGTH_SHORT).show();
                    return null;
                });
    }

    private void setUpPlane() {
        arFragment.setOnTapArPlaneListener(new BaseArFragment.OnTapArPlaneListener() {
            @Override
            public void onTapPlane(HitResult hitResult, Plane plane, MotionEvent motionEvent) {
                Anchor anchor = hitResult.createAnchor();
                AnchorNode anchorNode = new AnchorNode(anchor);
                anchorNode.setParent(arFragment.getArSceneView().getScene());
                createModel(anchorNode);
            }
        });
    }


    private void createModel(AnchorNode anchorNode){
        TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
        node.setParent(anchorNode);
        node.setRenderable(modelRenderable);
        node.select();
    }
}