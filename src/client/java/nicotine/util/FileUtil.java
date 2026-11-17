package nicotine.util;

import org.lwjgl.sdl.SDLDialog;
import org.lwjgl.sdl.SDL_DialogFileCallbackI;
import org.lwjgl.sdl.SDL_DialogFileFilter;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.system.Pointer;

import java.util.ArrayList;
import java.util.List;

import static nicotine.util.Common.mc;

public class FileUtil {

   public static List<String> convertFileList(long filelist) {
       List<String> files = new ArrayList<>();

       for (;;) {
           long filelistAddr = MemoryUtil.memGetAddress(filelist);

           if (filelistAddr == MemoryUtil.NULL) {
               return files;
           }

           filelist += Pointer.POINTER_SIZE;

           String file = MemoryUtil.memUTF8(filelistAddr);
           files.add(file);
       }
   }

   public static void openDialog(SDL_DialogFileCallbackI callback, String filterName, String filterPattern, boolean multiFile) {
       try (MemoryStack stack = MemoryStack.stackPush()) {
           SDL_DialogFileFilter.Buffer filters = SDL_DialogFileFilter.malloc(1, stack);
           filters.get(0).name(stack.UTF8(filterName)).pattern(stack.UTF8(filterPattern));
           SDLDialog.SDL_ShowOpenFileDialog(callback, MemoryUtil.NULL, mc.getWindow().handle(), filters, "", multiFile);
       }
   }

   public static void saveDialog(SDL_DialogFileCallbackI callback, String filterName, String filterPattern) {
       try (MemoryStack stack = MemoryStack.stackPush()) {
           SDL_DialogFileFilter.Buffer filters = SDL_DialogFileFilter.malloc(1, stack);
           filters.get(0).name(stack.UTF8(filterName)).pattern(stack.UTF8(filterPattern));
           SDLDialog.SDL_ShowSaveFileDialog(callback, MemoryUtil.NULL, mc.getWindow().handle(), filters, "");
       }
   }
}
