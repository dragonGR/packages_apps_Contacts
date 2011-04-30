/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.contacts.model;

import com.android.contacts.R;
import com.android.contacts.util.DateUtils;
import com.google.android.collect.Lists;

import android.content.ContentValues;
import android.content.Context;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Relation;
import android.view.inputmethod.EditorInfo;

public class GoogleAccountType extends BaseAccountType {
    public static final String ACCOUNT_TYPE = "com.google";
    protected static final int FLAGS_RELATION = EditorInfo.TYPE_CLASS_TEXT
    | EditorInfo.TYPE_TEXT_FLAG_CAP_WORDS | EditorInfo.TYPE_TEXT_VARIATION_PERSON_NAME;

    public GoogleAccountType(Context context, String resPackageName) {
        this.accountType = ACCOUNT_TYPE;
        this.resPackageName = null;
        this.summaryResPackageName = resPackageName;

        addDataKindStructuredName(context);
        addDataKindDisplayName(context);
        addDataKindPhoneticName(context);
        addDataKindNickname(context);
        addDataKindPhone(context);
        addDataKindEmail(context);
        addDataKindStructuredPostal(context);
        addDataKindIm(context);
        addDataKindOrganization(context);
        addDataKindPhoto(context);
        addDataKindNote(context);
        addDataKindWebsite(context);
        addDataKindSipAddress(context);
        addDataKindGroupMembership(context);
        addDataKindRelation(context);
        addDataKindEvent(context);
    }

    @Override
    protected DataKind addDataKindPhone(Context context) {
        final DataKind kind = super.addDataKindPhone(context);

        kind.typeColumn = Phone.TYPE;
        kind.typeList = Lists.newArrayList();
        kind.typeList.add(buildPhoneType(Phone.TYPE_MOBILE));
        kind.typeList.add(buildPhoneType(Phone.TYPE_WORK));
        kind.typeList.add(buildPhoneType(Phone.TYPE_HOME));
        kind.typeList.add(buildPhoneType(Phone.TYPE_MAIN));
        kind.typeList.add(buildPhoneType(Phone.TYPE_FAX_WORK).setSecondary(true));
        kind.typeList.add(buildPhoneType(Phone.TYPE_FAX_HOME).setSecondary(true));
        kind.typeList.add(buildPhoneType(Phone.TYPE_PAGER).setSecondary(true));
        kind.typeList.add(buildPhoneType(Phone.TYPE_OTHER));
        kind.typeList.add(buildPhoneType(Phone.TYPE_CUSTOM).setSecondary(true)
                .setCustomColumn(Phone.LABEL));

        kind.fieldList = Lists.newArrayList();
        kind.fieldList.add(new EditField(Phone.NUMBER, R.string.phoneLabelsGroup, FLAGS_PHONE));

        return kind;
    }

    @Override
    protected DataKind addDataKindEmail(Context context) {
        final DataKind kind = super.addDataKindEmail(context);

        kind.typeColumn = Email.TYPE;
        kind.typeList = Lists.newArrayList();
        kind.typeList.add(buildEmailType(Email.TYPE_HOME));
        kind.typeList.add(buildEmailType(Email.TYPE_WORK));
        kind.typeList.add(buildEmailType(Email.TYPE_OTHER));
        kind.typeList.add(buildEmailType(Email.TYPE_CUSTOM).setSecondary(true).setCustomColumn(
                Email.LABEL));

        kind.fieldList = Lists.newArrayList();
        kind.fieldList.add(new EditField(Email.DATA, R.string.emailLabelsGroup, FLAGS_EMAIL));

        return kind;
    }

    private DataKind addDataKindRelation(Context context) {
        DataKind kind = addKind(new DataKind(Relation.CONTENT_ITEM_TYPE,
                R.string.relationLabelsGroup, -1, 160, true,
                R.layout.text_fields_editor_view, R.string.add_relationship));
        kind.actionHeader = new RelationActionInflater();
        kind.actionBody = new SimpleInflater(Relation.NAME);

        kind.typeColumn = Relation.TYPE;
        kind.typeList = Lists.newArrayList();
        kind.typeList.add(buildRelationType(Relation.TYPE_ASSISTANT));
        kind.typeList.add(buildRelationType(Relation.TYPE_BROTHER));
        kind.typeList.add(buildRelationType(Relation.TYPE_CHILD));
        kind.typeList.add(buildRelationType(Relation.TYPE_DOMESTIC_PARTNER));
        kind.typeList.add(buildRelationType(Relation.TYPE_FATHER));
        kind.typeList.add(buildRelationType(Relation.TYPE_FRIEND));
        kind.typeList.add(buildRelationType(Relation.TYPE_MANAGER));
        kind.typeList.add(buildRelationType(Relation.TYPE_MOTHER));
        kind.typeList.add(buildRelationType(Relation.TYPE_PARENT));
        kind.typeList.add(buildRelationType(Relation.TYPE_PARTNER));
        kind.typeList.add(buildRelationType(Relation.TYPE_REFERRED_BY));
        kind.typeList.add(buildRelationType(Relation.TYPE_RELATIVE));
        kind.typeList.add(buildRelationType(Relation.TYPE_SISTER));
        kind.typeList.add(buildRelationType(Relation.TYPE_SPOUSE));
        kind.typeList.add(buildRelationType(Relation.TYPE_CUSTOM).setSecondary(true)
                .setCustomColumn(Relation.LABEL));

        kind.defaultValues = new ContentValues();
        kind.defaultValues.put(Relation.TYPE, Relation.TYPE_SPOUSE);

        kind.fieldList = Lists.newArrayList();
        kind.fieldList.add(new EditField(Relation.DATA, R.string.relationLabelsGroup,
                FLAGS_RELATION));

        return kind;
    }

    private DataKind addDataKindEvent(Context context) {
        DataKind kind = addKind(new DataKind(Event.CONTENT_ITEM_TYPE,
                    R.string.eventLabelsGroup, -1, 150, true,
                    R.layout.event_field_editor_view, R.string.add_event));
        kind.actionHeader = new EventActionInflater();
        kind.actionBody = new SimpleInflater(Event.START_DATE);

        kind.typeColumn = Event.TYPE;
        kind.typeList = Lists.newArrayList();
        kind.dateFormatWithoutYear = DateUtils.NO_YEAR_DATE_FORMAT;
        kind.dateFormatWithYear = DateUtils.FULL_DATE_FORMAT;
        kind.typeList.add(buildEventType(Event.TYPE_BIRTHDAY, true).setSpecificMax(1));
        kind.typeList.add(buildEventType(Event.TYPE_ANNIVERSARY, false));
        kind.typeList.add(buildEventType(Event.TYPE_OTHER, false));
        kind.typeList.add(buildEventType(Event.TYPE_CUSTOM, false).setSecondary(true)
                .setCustomColumn(Event.LABEL));

        kind.defaultValues = new ContentValues();
        kind.defaultValues.put(Event.TYPE, Event.TYPE_BIRTHDAY);

        kind.fieldList = Lists.newArrayList();
        kind.fieldList.add(new EditField(Event.DATA, R.string.eventLabelsGroup, FLAGS_EVENT));

        return kind;
    }

    @Override
    public int getHeaderColor(Context context) {
        return 0xff89c2c2;
    }

    @Override
    public int getSideBarColor(Context context) {
        return 0xff5bb4b4;
    }
}
