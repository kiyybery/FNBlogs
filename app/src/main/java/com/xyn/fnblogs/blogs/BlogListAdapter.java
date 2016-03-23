package com.xyn.fnblogs.blogs;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xyn.fnblogs.R;
import com.xyn.fnblogs.base.adapter.EfficientAdapter;
import com.xyn.fnblogs.base.adapter.EfficientRecyclerAdapter;
import com.xyn.fnblogs.base.adapter.EfficientViewHolder;
import com.xyn.fnblogs.image.ZImage;
import com.xyn.fnblogs.lib.ZDisplay;
import com.xyn.fnblogs.util.SQLiteHelper;

import java.util.List;

/**
 * Created by Administrator on 2016/3/23 0023.
 */
public class BlogListAdapter extends EfficientRecyclerAdapter<BlogEntity> {
    private int mAvatarSize;


    public BlogListAdapter(List<BlogEntity> objects) {
        super(objects);
        mAvatarSize = ZDisplay.getInstance().Dp2Px(38);
        setOnItemClickListener(new OnItemClickListener<BlogEntity>() {
            @Override
            public void onItemClick(EfficientAdapter<BlogEntity> adapter, View view, BlogEntity object, int position) {

                if (object.getId() > 0) {
                    BlogDetailActivity.actionStart(view.getContext(), object);

                    if (!SQLiteHelper.getInstance().isReadBlog(object.getId()))
                        ((TextView) view.findViewById(R.id.blog_item_title)).setTextColor(ZDisplay.getInstance().getFontColor(true));
                }
            }
        });
    }

    @Override
    public int getLayoutResId(int viewType) {
        return R.layout.blog_list_item;
    }

    @Override
    public Class<? extends EfficientViewHolder<? extends BlogEntity>> getViewHolderClass(int viewType) {
        return BlogViewHolder.class;
    }

    public class BlogViewHolder extends EfficientViewHolder<BlogEntity> {
        ImageView imgUserAvatar;
        TextView tvTitle;

        public BlogViewHolder(View itemView) {
            super(itemView);
            imgUserAvatar = findViewByIdEfficient(R.id.blog_item_avatar);
            tvTitle = findViewByIdEfficient(R.id.blog_item_title);
        }

        @Override
        protected void updateView(Context context, BlogEntity object) {
            ZImage.ready().want(object.getAuthorAvatar()).reSize(mAvatarSize, mAvatarSize).empty(R.mipmap.avatar_place_holder).into(imgUserAvatar);

            if (!TextUtils.isEmpty(object.getTitle())) {
                boolean isRead = SQLiteHelper.getInstance().isReadBlog(object.getId());
                tvTitle.setTextColor(ZDisplay.getInstance().getFontColor(isRead));
                tvTitle.setText(Html.fromHtml(object.getTitle()));
            }

            setText(R.id.blog_item_description, object.getAuthorName());
            setText(R.id.blog_item_comment, String.format("评 %d", object.getCommentAmount()));
            setText(R.id.blog_item_like, String.format("赞 %d", object.getRecommendAmount()));
        }
    }
}
