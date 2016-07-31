Android OpenGL-ES Experiments

An Android App that produces 3D Graphics on the screen using OpenGL-ES directly.

It started out as a learning exercise following the official tutorial, but has now migrated to being an exploration of the techniques involved in deriving a 3D model from an STL (3D printer) file exported from a CAD system, and working from first principles to
encapsulate this into a sensible multi-actor scene model, and to produce the graphics pipeline with low level OpenGL api calls. For example coding shaders on the fly.

This is part of the preparation for another of my GitHub projects - CADBOARD - that seeks to expose these CAD models into immersive Virtual Reality environments using the Google Cardboard platform.

If you're curious why I didn't make it easier for myself by using the Unity engine, it's because I have vision for the final distributed architecture and user interaction idioms that will require finely grained access to how things work under the hood. You can read more about those aspirations here https://github.com/peterhoward42/cadboard .
