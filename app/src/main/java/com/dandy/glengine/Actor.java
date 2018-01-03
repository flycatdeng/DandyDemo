package com.dandy.glengine;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.dandy.helper.android.LogHelper;
import com.dandy.helper.java.PendingThreadAider;
import com.dandy.helper.java.math.Vec3;
import com.dandy.helper.opengl.MVPMatrixAider;
import com.dandy.helper.opengl.TextureHelper;
import com.dandy.helper.opengl.TextureOptions;

/**
 * <pre>
 * </pre>
 * <pre>
 * Created by Dandy
 * Wechat: flycatdeng
 * </pre>
 */

public class Actor implements IGLActor, IActorMatrixOperation, IActorPendding {
    private static final String TAG = "Actor";
    protected Context mContext;
    /**
     * Surface的宽高(View容器的宽高)
     */
    protected int mSurfaceWidth, mSurfaceHeight;
    private PendingThreadAider mRunOnceOnDraw = new PendingThreadAider();//在使用program的时候(onDraw)将PendingThreadAider内的要执行的任务都执行完
    private PendingThreadAider mRunOnceBeforeDraw = new PendingThreadAider();//调用onDrawFrame绘制实际内容(onDraw)之前将PendingThreadAider内的要执行的任务都执行完
    private PendingThreadAider mCreateProgramBeforeDraw = new PendingThreadAider();
    protected MVPMatrixAider mMatrixAider = new MVPMatrixAider();//矩阵操作帮助类
    protected Material mMaterial;
    protected int mVertexCount = 0;//顶点数量
    protected int mProgramID = -1;// 自定义渲染管线着色器程序id
    protected int mTextureID = -1;//纹理ID
    protected String mDefaultMaterialName = "gles_engine_material/default_simple.mat";//默认使用的shader信息文件路劲
    protected TextureOptions mDefaultTextureOptions;//纹理采样选项
    private Actor mParentActor;
    private boolean mIsMaterialSetFromOutside = false;//是否是从外部设置的shader信息
    private boolean mIsSurfaceCreated = false;//onSurfaceCreated是否已经被调用了
    private RequestRenderListener mRequestRenderListener;//请求渲染监听回调

    public Actor(Context context) {
        mContext = context;
        mMatrixAider.setInitStack();//一定要在构造器初始化的时候就初始化矩阵帮助类，否则可能在其他地方调用setTranslate等方法时如果没有初始化，那就无效了
    }

    /***********************暂时没啥用******************************************/
    public Actor getParentActor() {
        return mParentActor;
    }

    public void setParent(Actor parent) {
        mParentActor = parent;
    }

    protected boolean mIsResumed = false;

    public void onResume() {
        mIsResumed = true;
    }

    public void onPause() {
        mIsResumed = false;
    }

    public void onDestroy() {
        GLES20.glDeleteProgram(mProgramID);
        if (mCreateProgramBeforeDraw != null) {
            mCreateProgramBeforeDraw.destroy();
            mCreateProgramBeforeDraw = null;
        }
        if (mRunOnceBeforeDraw != null) {
            mRunOnceBeforeDraw.destroy();
            mRunOnceBeforeDraw = null;
        }
        if (mRunOnceOnDraw != null) {
            mRunOnceOnDraw.destroy();
            mRunOnceOnDraw = null;
        }
        if (mMatrixAider != null) {
            mMatrixAider.onDestroy();
            mMatrixAider = null;
        }
        if (mMaterial != null) {
            mMaterial.destroy();
            mMaterial = null;
        }
        mParentActor = null;
        mRequestRenderListener = null;
    }

    /**
     * 设置纹理，回收Bitmap对象
     *
     * @param bitmap
     */
    public void setTexture(Bitmap bitmap) {
        setTexture(bitmap, true);
    }

    /**
     * 设置纹理
     *
     * @param bitmap     纹理Bitmap
     * @param recycleBmp 是否回收该bitmap
     */
    public void setTexture(final Bitmap bitmap, final boolean recycleBmp) {
        setTexture(bitmap, mDefaultTextureOptions, recycleBmp);
    }

    public void setTexture(final Bitmap bitmap, final TextureOptions options, final boolean recycleBmp) {
        addRunOnceBeforeDraw(new Runnable() {
            @Override
            public void run() {
                if (mTextureID == -1) {
                    mTextureID = TextureHelper.initTextureID(bitmap, options, recycleBmp);
                } else {
                    GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
                    TextureHelper.changeTextureImage(bitmap);
                }
            }
        });
    }

    /**
     * <pre>
     *     初始化纹理，只有当mTextureID为-1的时候才会更改mTextureID的值
     *     如果想直接更换mTextureID的值，请调用{@link #setTexture(int)}
     * </pre>
     */
    public void initTexture(int textureId) {
        if (mTextureID == -1) {
            mTextureID = textureId;
        }
    }

    /**
     * 设置纹理
     */
    public void setTexture(int textureId) {
        mTextureID = textureId;
    }

    /**
     * 绘制，此时的mProgramID已经得到了，而且已经调用了GLES20.glUseProgram(mProgramID);
     * 子类需要重写这个方法去实现自己的绘制
     */
    protected void onDraw() {
        GLES20.glUseProgram(mProgramID);
        dealRunOnceOnDraw();
    }

    public void setMaterialFromAssets(final String materialFile) {
        mIsMaterialSetFromOutside = true;
        addRunOnceCreateProgramBeforeDraw(new Runnable() {
            @Override
            public void run() {
                mMaterial = Material.obtainFromAssets(mContext, materialFile);
                mProgramID = mMaterial.getProgramID();
                LogHelper.d(TAG, "setMaterialFromAssets materialFile=" + materialFile + " mProgramID=" + mProgramID);
                onShaderLocationInit();//初始化完program之后就获取对应的location
            }
        });
    }

    protected void onShaderLocationInit() {
    }

    protected int getMaterialHandler(String propertyName) {
        if (mMaterial != null) {
            Integer result = mMaterial.getHandlerByPropertyName(propertyName);
            return result == null ? -1 : result;
        }
        return -1;
    }

    //*************************************************IActorPendding 实现*************************/
    @Override
    public void addRunOnceCreateProgramBeforeDraw(Runnable runnable) {
        mCreateProgramBeforeDraw.addToPending(runnable);
    }

    @Override
    public void dealCreateProgramBeforeDraw() {
        mCreateProgramBeforeDraw.runPendings();
    }

    @Override
    public void addRunOnceBeforeDraw(Runnable runnable) {
        mRunOnceBeforeDraw.addToPending(runnable);
    }

    @Override
    public void dealRunOnceBeforeDraw() {
        mRunOnceBeforeDraw.runPendings();
    }

    @Override
    public void addRunOnceOnDraw(Runnable runnable) {
        mRunOnceOnDraw.addToPending(runnable);
    }

    @Override
    public void dealRunOnceOnDraw() {
        mRunOnceOnDraw.runPendings();
    }

    //*************************************************IGLActor 实现*************************/
    @Override
    public void onSurfaceCreated() {
        mIsSurfaceCreated = true;
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        mSurfaceWidth = width;
        mSurfaceHeight = height;
    }

    @Override
    public void onDrawFrame() {
        //如果不是从外面设置的材质则优先加载默认材质，否则的话就是从外面设置的材质，如果出现问题，接下来还有一次设置默认材质的方式来保底
        if (mProgramID == -1 && !mIsMaterialSetFromOutside) {//使用默认的材质信息
            setMaterialFromAssets(mDefaultMaterialName);
        }
        //在绘制之前，不管你是从外边设置的材质，还是使用默认的材质，都要去初始化program
        dealCreateProgramBeforeDraw();//这个方法可能被外面直接调用哦
        if (mProgramID == -1) {
            throw new RuntimeException("mProgramID == -1");
        }
        dealRunOnceBeforeDraw();
        onDraw();
    }

    @Override
    public boolean isSurfaceCreated() {
        return mIsSurfaceCreated;
    }

    @Override
    public void requestRender() {
        if (mRequestRenderListener != null) {
            mRequestRenderListener.onRequestRenderCalled();
        }
    }

    @Override
    public void setRequestRenderListener(RequestRenderListener listener) {
        mRequestRenderListener = listener;
    }

    @Override
    public RequestRenderListener getRequestRenderListener() {
        return mRequestRenderListener;
    }

    //*************************************************IActorMatrixOperation 实现*************************/
    private Vec3 mPreLocation = new Vec3();

    @Override
    public void translate(float x, float y, float z) {
        mMatrixAider.translate(x, y, z);
        mPreLocation.add(x, y, z);
    }

    @Override
    public void translate(Vec3 offset) {
        mMatrixAider.translate(offset.x, offset.y, offset.z);
        mPreLocation.add(offset);
    }

    @Override
    public void setTranslation(float x, float y, float z) {
        mMatrixAider.translate(x - mPreLocation.x, y - mPreLocation.y, z - mPreLocation.z);
        mPreLocation = new Vec3(x, y, z);
    }

    @Override
    public void rotate(float angle, float x, float y, float z) {
        mMatrixAider.rotate(angle, x, y, z);
    }

    @Override
    public void scale(float x, float y, float z) {
        mMatrixAider.scale(x, y, z);
    }

    @Override
    public void scale(Vec3 scale) {
        mMatrixAider.scale(scale.x, scale.y, scale.z);
    }

    @Override
    public void scale(float scale) {
        mMatrixAider.scale(scale, scale, scale);
    }

    @Override
    public void setCamera(float cx, float cy, float cz, float tx, float ty, float tz, float upx, float upy, float upz) {
        mMatrixAider.setCamera(cx, cy, cz, tx, ty, tz, upx, upy, upz);
    }

    @Override
    public void setProjectFrustum(float left, float right, float bottom, float top, float near, float far) {
        mMatrixAider.setProjectFrustum(left, right, bottom, top, near, far);
    }

    @Override
    public void setProjectOrtho(float left, float right, float bottom, float top, float near, float far) {
        mMatrixAider.setProjectOrtho(left, right, bottom, top, near, far);
    }

    @Override
    public float[] getMVPMatrix() {
        return mMatrixAider.getMVPMatrix();
    }

    @Override
    public void setMVPMatrix(float[] matrix) {
        mMatrixAider.setMVPMatrix(matrix);
    }

    @Override
    public float[] getModelMatrix() {
        return mMatrixAider.getModelMatrix();
    }

    @Override
    public float[] getViewMatrix() {
        return mMatrixAider.getViewMatrix();
    }

    @Override
    public float[] getModelViewMatrix() {
        return mMatrixAider.getModelViewMatrix();
    }

    @Override
    public float[] getProjectMatrix() {
        return mMatrixAider.getProjectMatrix();
    }
}
