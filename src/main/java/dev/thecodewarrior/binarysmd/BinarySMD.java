package dev.thecodewarrior.binarysmd;

import dev.thecodewarrior.binarysmd.studiomdl.SMDFile;
import dev.thecodewarrior.binarysmd.studiomdl.SMDFileBlock;
import dev.thecodewarrior.binarysmd.studiomdl.SkeletonBlock;
import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageUnpacker;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BinarySMD {
    /**
     * @param smd The text contents of the SMD file
     * @param packer The destination for the packed file
     */
    public static void pack(String smd, MessagePacker packer) {

    }

    /**
     * @param packer The source for the packed file
     * @return The text contents of the SMD file
     */
    public static String pack(MessageUnpacker packer) {

        return "";
    }

    public static void optimize(SMDFile file) {
        SkeletonBlock block = null;
        for(SMDFileBlock it : file.blocks) {
            if(it instanceof SkeletonBlock) {
                block = (SkeletonBlock) it;
                break;
            }
        }
        if(block == null)
            return;

        Map<Integer, SkeletonBlock.BoneState> currentState = new HashMap<>();

        for (SkeletonBlock.Keyframe keyframe : block.keyframes) {
            Iterator<SkeletonBlock.BoneState> stateIter = keyframe.states.iterator();
            while (stateIter.hasNext()) {
                SkeletonBlock.BoneState state = stateIter.next();
                if (state.equals(currentState.get(state.bone))) {
                    stateIter.remove();
                    continue;
                }
                currentState.put(state.bone, state);
            }
        }
    }
}
